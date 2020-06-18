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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Server {
    private static Logger logger = LogManager.getLogger();
    private final int PORT = 40000;
    private final Protocol protocol;
    private OscAdapter oscAdapter;
    private TransformationProcessing transformationProcessing;
    private ServerSocket serverSocket;
    private Socket client;
    private BufferedOutputStream bufferedOut;
    private InputStream in;
    private Game game;

    public Server(Protocol protocol, OscAdapter oscAdapter) {
        this.protocol = protocol;
        this.oscAdapter = oscAdapter;
    }

    public void start() throws IOException {
        logger.info("Starting the server...");
        serverSocket = new ServerSocket(PORT);

        // assume only one client and the client is all-trusted
        logger.info("Awaiting client...");
        client = serverSocket.accept();
        bufferedOut = new BufferedOutputStream(client.getOutputStream());
        in = client.getInputStream();

        logger.info("Client connected, initializing game...");
        transformationProcessing = new TransformationProcessing();
        transformationProcessing.start();
        initializeGame();
        listeningToClientUpdates();
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


    private void listeningToClientUpdates() {
        new Thread(() -> {
            try {
                while (!serverSocket.isClosed()) {
                    UpdateLocationRequest update = protocol.parseUpdateLocationRequest(in);
                    logger.info("New UpdateLocationRequest received and deserialized.");
                    World world = game.getWorld();
                    world.updatePlayerLocation(update.point, update.angle);
                }
            } catch (IOException e) {
                logger.error(e);
            }
        }).start();
    }

    private class TransformationProcessing extends Thread {
        private Logger logger = LogManager.getLogger();
        private boolean running = false;
        private Vector3 latestPoint = new Vector3(-1, -1, -1);
        private double latestAngle = -1;

        @Override
        public void run() {
            running = true;
            while (running) {
                try {
                    Thread.sleep(500);
                    Player p = game.getWorld().getPlayer();

                    List<Vector3> newVectors = viewTransform(p.getPoint(), p.getAngle());
                    updateSpriteLocationsFromVectors(newVectors);
                    oscAdapter.updateTrack(newVectors);
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

        private void updateSpriteLocationsFromVectors(List<Vector3> newVectors) {
            IntStream.range(0, newVectors.size())
                    .forEach(i -> game.getSprites().get(i).setPoint(newVectors.get(i)));
        }

        private void logSprites() {
            logger.trace(game.getSprites().stream()
                    .map(Object::toString).collect(Collectors.joining("\n")));
        }

        public void stopProcessing() {
            running = false;
        }

    }

}

