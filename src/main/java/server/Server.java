package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.protocol.Protocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static Logger logger = LogManager.getLogger();
    private final int PORT = 40000;
    private final Protocol protocol;
    private OscAdapter oscAdapter;
    private ServerSocket serverSocket;

    public Server(Protocol protocol, OscAdapter oscAdapter) {
        this.protocol = protocol;
        this.oscAdapter = oscAdapter;
    }

    public void start() throws IOException {
        logger.info("Starting the server...");
        serverSocket = new ServerSocket(PORT);

        while (!serverSocket.isClosed()) {
            // assume only one client and the client is all-trusted
            logger.info("Awaiting next client...");
            Socket client = serverSocket.accept();
            logger.info("A new client accepted.");
            ClientService clientService = new ClientService(protocol, oscAdapter, client);
            clientService.start();
        }
    }


}

