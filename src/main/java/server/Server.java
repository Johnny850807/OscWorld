package server;

import server.protocol.Protocol;
import server.protocol.Protocol.UpdateLocationRequest;
import world.Game;
import world.Player;
import world.Vector3;
import world.World;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final int PORT = 40000;
    private final Protocol protocol;
    private TransformationProcessing transformationProcessing;
    private ServerSocket serverSocket;
    private Socket client;
    private BufferedOutputStream bufferedOut;
    private InputStream in;
    private Game game;

    public Server(Protocol protocol) {
        this.protocol = protocol;
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
        game.initializeAndStart();
        try {
            protocol.writeInitializedSprites(bufferedOut, game.getSprites());
            bufferedOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void listeningToClientUpdates() {
        new Thread(() -> {
            try {
                UpdateLocationRequest update = protocol.parseUpdateLocationRequest(in);
                World world = game.getWorld();
                world.updatePlayerLocation(update.point, update.angle);

            } catch (IOException e) {
                e.printStackTrace();
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

                    // only refresh when the player states actually updated
                    if (!p.getPoint().equals(latestPoint) || p.getAngle() != latestAngle) {
                        latestPoint = p.getPoint();
                        latestAngle = p.getAngle();
                    }

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

