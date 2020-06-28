package server;

import server.protocol.V1Protocol;

import java.io.IOException;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class Main {
    public static void main(String[] args) throws IOException {
        String host = System.getenv("oscHost");
        Server server = new Server(new V1Protocol(),
                new OscAdapterImpl(host == null ? "localhost" : host, 9001));
        server.start();
    }
}
