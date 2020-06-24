package world;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Game {
    private World world;

    public void initializeAndStart(int numOfObjects) {
        world = new World();
        int[] typeIds = SoundSprites.Types.getAll();
        Random random = new Random();
        for (int i = 0; i < numOfObjects; i++) {
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

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
