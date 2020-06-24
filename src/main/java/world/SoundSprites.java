package world;

import java.util.HashMap;
import java.util.Map;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class SoundSprites {
    private final static double DEFAULT_VOLUME = 0.4;

    public interface Types {
        int BIRD = 1;

        static int[] getAll() {
            return new int[]{BIRD};
        }
    }

    public interface SoundIDs {
        int BIRD = 1;
        static int[] getAll() {
            return new int[]{BIRD};
        }
    }

    private static final Map<Integer, Sprite> spriteMap = new HashMap<>();

    static {
        spriteMap.put(Types.BIRD, new SoundSprite(Types.BIRD, DEFAULT_VOLUME, SoundIDs.BIRD));
    }

    public static Sprite createSprite(int typeId) {
        return spriteMap.get(typeId).clone();
    }
}
