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
public class ClientService extends Thread {
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

            logger.info("Client connected, initializing game...");
            worldSimulationProcessing = new WorldSimulationProcessing();
            worldSimulationProcessing.start();
            initializeGame();
            listenToClientUpdates();
        } catch (IOException err) {
            logger.error(err);
        }
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

    private void listenToClientUpdates() {
        clientUpdatesListenerThread = new Thread(() -> {
            try {
                while (!client.isClosed()) {
                    UpdateLocationRequest update = protocol.parseUpdateLocationRequest(in);
                    logger.info("New UpdateLocationRequest received and deserialized.");
                    World world = game.getWorld();
                    world.updatePlayerLocation(update.point, update.angle);
                }
                logger.fatal("Stop listening to the client.");
                clearUpClientService();
            } catch (IOException e) {
                logger.error(e);
            }
        });
        clientUpdatesListenerThread.start();
    }

    private void clearUpClientService() {
        logger.traceEntry("Clearing up the client service.");
        game = null;
        worldSimulationProcessing.interrupt();
        worldSimulationProcessing = null;
        clientUpdatesListenerThread.interrupt();
        clientUpdatesListenerThread = null;
        logger.traceExit("Cleared up the client service.");
    }

    private class WorldSimulationProcessing extends Thread {

        private Logger logger = LogManager.getLogger();
        private Vector3 latestPoint = new Vector3(-1, -1, -1);
        private double latestAngle = -1;

        @Override
        public void run() {
            while (!client.isClosed()) {
                try {
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
                } catch (InterruptedException e) {
                    logger.error(e);
                }
            }
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
