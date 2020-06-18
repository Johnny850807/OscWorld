package server;

import matrices.Rotation;
import matrices.Transformation;
import matrices.Translation;
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

public class Server {
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
        serverSocket = new ServerSocket(PORT);

        // assume only one client and the client is all-trusted
        client = serverSocket.accept();
        bufferedOut = new BufferedOutputStream(client.getOutputStream());
        in = client.getInputStream();

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void listeningToClientUpdates() {
        new Thread(() -> {
            while (!serverSocket.isClosed()) {
                try {
                    UpdateLocationRequest update = protocol.parseUpdateLocationRequest(in);
                    World world = game.getWorld();
                    world.updatePlayerLocation(update.point, update.angle);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private class TransformationProcessing extends Thread {
        private boolean running = false;
        private Vector3 latestPoint;
        private double latestAngle;

        @Override
        public void run() {
            running = true;
            while (running) {
                try {
                    Thread.sleep(500);
                    Player p = game.getWorld().getPlayer();

                    latestPoint = p.getPoint();
                    latestAngle = p.getAngle();

                    Transformation transformation =
                            new Translation(-latestPoint.x, -latestPoint.y, -latestPoint.z)
                                    .compose(Rotation.zAxis(latestAngle))
                                    .compose(new Translation(latestPoint.x, latestPoint.y, latestPoint.z));

                    List<Vector3> newVectors = transformation.transform(
                            game.getSprites().stream()
                                    .map(Sprite::getPoint).collect(Collectors.toList()));

                    oscAdapter.updateTrack(newVectors);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void stopProcessing() {
            running = false;
        }

    }

}

