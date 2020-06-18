package server;

import server.protocol.V1Protocol;

import java.io.IOException;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Server server = new Server(new V1Protocol(), new MockOscAdapter());
        server.start();
    }
}
