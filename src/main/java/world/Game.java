package world;

import java.util.Collection;
import java.util.Random;

public class Game {
    private World world;

    @SuppressWarnings("unchecked")
    public void initializeAndStart(int numOfObjects) {
        world = new World();
        Class<? extends AISprite>[] types = new Class[]{Bird.class};
        Random random = new Random();
        for (int i = 0; i < numOfObjects; i++) {
            try {
                AISprite aISprite = types[random.nextInt(types.length)].newInstance();
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

    public Collection<Sprite> getSprites() {
        return world.getSprites();
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
