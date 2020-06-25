package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.protocol.Protocol;
import server.protocol.Protocol.UpdateLocationRequest;
import world.Game;
import world.SoundSprite;
import world.Sprite;
import world.World;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

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
        initAndStartGame();
    }

    private void initAndStartGame() {
        game = new Game();
        game.addUpdateListener(this::onGameLoopUpdate);
        game.initialize(20);
        try {
            protocol.writeInitializedSprites(bufferedOut, game.getSprites());
            bufferedOut.flush();
            game.start();
            logger.info("Game initialized and sent to client.");
        } catch (IOException e) {
            logger.error(e);
        }
    }

    private void onGameLoopUpdate(List<Sprite> viewSprites) {
        oscAdapter.updateSoundTrack(viewSprites.stream()
                .filter(s -> s instanceof SoundSprite)
                .map(s -> (SoundSprite) s).collect(Collectors.toList()));
    }

    @Override
    public void onGameOverRequest() {
        cleanUpGame();
    }

    private void cleanUpGame() {
        logger.traceEntry("Stopping...");
        oscAdapter.clearAll();
        game.stop();
        game = null;
        System.gc();
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

}
