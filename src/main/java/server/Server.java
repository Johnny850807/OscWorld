package server;

import world.Game;
import server.protocol.Protocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final int PORT = 40000;
    private final Protocol protocol;
    private ServerSocket serverSocket;
    private Socket client;
    private Game game;

    public Server(Protocol protocol) {
        this.protocol = protocol;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(PORT);

        // assume only one client and the client is all-trusted
        client = serverSocket.accept();
        initializeGame();
        listeningToClientUpdates();
    }

    private void initializeGame() {
        game = new Game();
        game.initializeAndStart();

        protocol.packInitializedSprites(game.getSprites());
    }

    private void sendInitializedSprites() {

    }

    private void listeningToClientUpdates() {

    }
}

