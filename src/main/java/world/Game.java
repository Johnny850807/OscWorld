package world;

import java.util.*;
import java.util.stream.Collectors;

public class Game {
    private World world;
    private ViewportProcessing viewportProcessing = new ViewportProcessing(this);
    private Collection<UpdateListener> updateListeners = Collections.synchronizedSet(new HashSet<>());
    private boolean running;

    public void addUpdateListener(UpdateListener listener) {
        updateListeners.add(listener);
    }

    public void initialize(int numOfSprites /*TODO the world will be restricted*/) {
        randomizeWorld(numOfSprites);
    }

    private void randomizeWorld(int numOfSprites) {
        world = new World();
        int[] typeIds = SoundSprites.Types.getAll();
        Random random = new Random();
        for (int i = 0; i < numOfSprites; i++) {
            int typeId = typeIds[random.nextInt(typeIds.length)];
            double min = -100, max = 100;
            Sprite sprite = SoundSprites.createSprite(typeId);
            sprite.setPoint(new Vector3(
                    Math.random() * ((max - min) + 1) + min,
                    Math.random() * ((max - min) + 1) + min,
                    Math.random() * ((max - min) + 1) + min)); //TODO Ranged randomization
            world.addSprite(sprite);
        }
    }

    public void start() {
        running = true;
        new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep(30);
                    viewportProcessing.process();
                    getSprites().forEach(Sprite::update);
                    updateListeners.forEach(UpdateListener::opUpdate);
                } catch (InterruptedException ignored) {}
            }
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

    public interface UpdateListener {
        void opUpdate();
    }

}
