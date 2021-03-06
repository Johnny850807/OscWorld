package world;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.ArrayUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class Game {
    private Logger logger = LogManager.getLogger();
    private World world;
    private ViewportTransformer viewportTransformer = new ViewportTransformer();
    private Collection<GameListener> gameListeners = Collections.synchronizedSet(new HashSet<>());
    private boolean running;

    private static final Vector3[] INIT_POINT_CANDIDATES = {
            new Vector3(-5, 0.89, -5),
            new Vector3(8.77, 1.47, -6.7),
            new Vector3(6.38, 1.84, 24.42),
            new Vector3(32.5, 1.03, -7.4),
            new Vector3(-0.09, 1.87, 31.03),
            new Vector3(18.73, 1.09, 30.04),
            new Vector3(16.3, 1.462, -0.6),
            new Vector3(27.9, 1.59, 12.82),
            new Vector3(36.2, 1.4, 14.61),
            new Vector3(23, 1.3, 2.4),
            new Vector3(37.93, 1.65, 38.08),
            new Vector3(-3.06, 1.55, -11.43)
    };

    public void addGameListener(GameListener listener) {
        gameListeners.add(listener);
    }

    public void initialize() {
        randomizeWorld();
    }

    private void randomizeWorld() {
        world = new World();
        int[] typeIds = SoundSprites.Types.getAllAnimals();
        List<Vector3> shuffledPoints = Arrays.asList(INIT_POINT_CANDIDATES);
        int[] shuffledTypeIds = ArrayUtils.shuffle(typeIds);
        Collections.shuffle(shuffledPoints);

        for (int i = 0; i < 4; i++) {
            int targetId = shuffledTypeIds[i];
            Sprite targetSprite = SoundSprites.createSprite(targetId);
            targetSprite.setPoint(shuffledPoints.get(i));
            world.addSprite(targetSprite);
        }

        logger.info("Animals: " +
                world.getSprites().stream()
                .map(s -> SoundSprites.getName(s.getTypeId()))
                .collect(Collectors.joining(", ")));

        // rivers
        world.addSprite(SoundSprites.createSprite(
                SoundSprites.Types.RIVER1, new Vector3(0, 0, 9.6)));
        world.addSprite(SoundSprites.createSprite(
                SoundSprites.Types.RIVER2, new Vector3(-12.1, 0, 29.3)));

    }

    public void start() {
        running = true;
        new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep(16);
                    List<Sprite> viewSprites = viewportTransformer.transform(getSprites(), getPlayer());
                    getSprites().forEach(Sprite::update);
                    gameListeners.forEach(l -> l.opUpdate(viewSprites));
                } catch (InterruptedException ignored) {}
            }
            gameListeners.forEach(GameListener::onGameOver);
        }).start();
    }

    public void stop() {
        running = false;
    }

    public List<Sprite> getSprites() {
        return world.getSprites();
    }

    @SuppressWarnings("unchecked")
    public <T extends Sprite> List<T> getSprites(Class<T> spriteType) {
        return world.getSprites().stream()
                .filter(s -> spriteType.isAssignableFrom(s.getClass()))
                .map(s -> (T) s)
                .collect(Collectors.toList());
    }

    public Player getPlayer() {
        return getWorld().getPlayer();
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public interface GameListener {
        void opUpdate(List<Sprite> viewSprites);
        void onGameOver();
    }

}
