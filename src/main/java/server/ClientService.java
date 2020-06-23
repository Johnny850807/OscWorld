package server;

import matrices.Rotation;
import matrices.Transformation;
import matrices.Translation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.protocol.Protocol;
import server.protocol.Protocol.UpdateLocationRequest;
import world.*;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class ClientService extends Thread implements Protocol.RequestHandler {
    private Logger logger = LogManager.getLogger();  //TODO use an effective name (i.e. clearly differentiate the client)

    private Protocol protocol;
    private OscAdapter oscAdapter;

    private Socket client;
    private BufferedOutputStream bufferedOut;
    private InputStream in;
    private Game game;
    private WorldSimulationProcessing worldSimulationProcessing;
    private Thread clientUpdatesListenerThread;

    public ClientService(Protocol protocol, OscAdapter oscAdapter, Socket client) {
        this.protocol = protocol;
        this.oscAdapter = oscAdapter;
        this.client = client;
    }

    @Override
    public void run() {
        logger.info("A client is running...");
        try {
            bufferedOut = new BufferedOutputStream(client.getOutputStream());
            in = client.getInputStream();
            listenToClientUpdates();
        } catch (IOException err) {
            logger.error(err);
        }
    }

    private void listenToClientUpdates() {
        clientUpdatesListenerThread = new Thread(() -> {
            try {
                while (!client.isClosed()) {
                    protocol.handleNextRequest(in, this);
                }
            } catch (IOException e) {
                logger.fatal("Stop listening to the client.");
                cleanUpGame();
            }
        });
        clientUpdatesListenerThread.start();
    }

    @Override
    public void onStartGameRequest() {
        logger.info("Client connected, initializing game...");
        worldSimulationProcessing = new WorldSimulationProcessing();
        worldSimulationProcessing.start();
        initializeGame();
    }

    private void initializeGame() {
        game = new Game();
        game.initializeAndStart(20);
        try {
            protocol.writeInitializedSprites(bufferedOut, game.getSprites());
            bufferedOut.flush();
            logger.info("Game initialized and sent to client.");
        } catch (IOException e) {
            logger.error(e);
        }
    }

    @Override
    public void onGameOverRequest() {
        cleanUpGame();
    }

    private void cleanUpGame() {
        logger.traceEntry("Stopping...");
        try {
            if (worldSimulationProcessing != null) {
                worldSimulationProcessing.interrupt();
                worldSimulationProcessing.join();
                worldSimulationProcessing = null;
            }
        } catch (InterruptedException ignored) { }
        oscAdapter.clearAll();
        game = null;

        logger.traceExit("Stopped.");
    }

    @Override
    public void onUpdateLocationRequest(UpdateLocationRequest updateLocationRequest) {
        logger.info("UpdateLocationRequest");
        World world = game.getWorld();
        world.updatePlayerLocation(updateLocationRequest.point, updateLocationRequest.angle);
    }

    @Override
    public void onPlaySoundRequest(Protocol.PlaySoundRequest playSoundRequest) {
        logger.info("PlaySoundRequest");
        oscAdapter.playSound(playSoundRequest.soundId);
    }

    private class WorldSimulationProcessing extends Thread {

        private Logger logger = LogManager.getLogger();
        private Vector3 latestPoint = new Vector3(-1, -1, -1);
        private double latestAngle = -1;

        @Override
        public void run() {
            try {
                while (!client.isClosed()) {
                    Thread.sleep(500);
                    Player p = game.getWorld().getPlayer();

                    List<Vector3> viewSpriteLocations = viewTransform(p.getPoint(), p.getAngle());
                    updateSprites(viewSpriteLocations);

                    oscAdapter.updateSoundTrack(game.getSprites(SoundSprite.class));
                    logger.trace("Objects Streamed to {} successfully.", oscAdapter.getClass().getSimpleName());

                    if (!latestPoint.equals(p.getPoint()) || latestAngle != p.getAngle()) {
                        logSprites();
                    }

                    latestPoint = p.getPoint();
                    latestAngle = p.getAngle();
                }
            } catch (InterruptedException ignored) { }
            logger.fatal("Stop running.");
        }

        private List<Vector3> viewTransform(Vector3 newPlayerPoint, double newAngle) {
            Transformation transformation =
                    new Translation(-newPlayerPoint.x, -newPlayerPoint.y, -newPlayerPoint.z)
                            .compose(Rotation.zAxis(newAngle))
                            .compose(new Translation(newPlayerPoint.x, newPlayerPoint.y, newPlayerPoint.z));

            return transformation.transform(
                    game.getSprites().stream()
                            .map(Sprite::getPoint).collect(Collectors.toList()));
        }

        private void updateSprites(List<Vector3> viewSpriteLocations) {
            game.getSprites().forEach(Sprite::update);
            IntStream.range(0, viewSpriteLocations.size())
                    .forEach(i -> game.getSprites().get(i).setPoint(viewSpriteLocations.get(i)));
        }

        private void logSprites() {
            logger.trace(game.getSprites().stream()
                    .map(Object::toString).collect(Collectors.joining("\n")));
        }

    }
}
