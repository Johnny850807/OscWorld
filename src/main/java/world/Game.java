package world;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Game {
    private World world;

    @SuppressWarnings("unchecked")
    public void initializeAndStart(int numOfObjects) {
        world = new World();
        Class<? extends SoundSprite>[] types = new Class[]{Bird.class};
        Random random = new Random();
        for (int i = 0; i < numOfObjects; i++) {
            try {
                SoundSprite aISprite = types[random.nextInt(types.length)].newInstance();
                double min = -100, max = 100;
                aISprite.setPoint(new Vector3(
                        Math.random() * ((max - min) + 1) + min,
                        Math.random() * ((max - min) + 1) + min,
                        Math.random() * ((max - min) + 1) + min));
                world.addSprite(aISprite);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Sprite> getSprites() {
        return world.getSprites();
    }

    @SuppressWarnings("unchecked")
    public <T extends Sprite> List<T> getSprites(Class<T> spriteType) {
        return world.getSprites().stream()
                .filter(s -> spriteType.isAssignableFrom(s.getClass()))
                .map(s -> (T)s)
                .collect(Collectors.toList());
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
