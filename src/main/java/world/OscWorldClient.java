package world;

import server.OscAdapter;
import server.OscAdapterImpl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class OscWorldClient {
    public static void main(String[] args) throws IOException {
        Game game = new Game();
        OscAdapter oscAdapter = new OscAdapterImpl("192.168.43.163", 9001);
        game.addGameListener(new Game.GameListener() {
            @Override
            public void opUpdate(List<Sprite> viewSprites) {
                oscAdapter.updateSoundTrack(
                        viewSprites.stream()
                                .filter(s -> s instanceof SoundSprite)
                                .map(s -> (SoundSprite) s)
                                .collect(Collectors.toList()));
            }

            @Override
            public void onGameOver() {
                oscAdapter.clearAll();
            }
        });
        game.initialize();
        game.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Clear all");
            oscAdapter.clearAll();
        }));
    }
}
