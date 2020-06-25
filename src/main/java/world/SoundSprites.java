package world;

import world.behaviors.LinearVolumeBehavior;
import world.behaviors.ProbablyVolumeWaveTriggerBehavior;
import world.behaviors.ProbablyUpdateBehavior;
import world.behaviors.SinVolumeBehavior;

import java.util.HashMap;
import java.util.Map;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class SoundSprites {
    private final static double DEFAULT_VOLUME = 0.3;

    public interface Types {
        int CAT = 0;
        int DOG = 1;
        int ELEPHANT = 2;
        int GOAT = 3;
        int HORSE = 4;
        int LION = 5;
        int MOUSE = 6;
        int SPARROW = 7;
        int TIGER = 8;
        int WOLF = 9;

        int RIVER = 100;


        static int[] getAllAnimals() {
            return new int[]{
                    CAT, DOG, ELEPHANT, GOAT, HORSE, LION, MOUSE, SPARROW, TIGER, WOLF
            };
        }

        static int[] getAllSurroundings() {
            return new int[]{
                    RIVER
            };
        }
    }

    public static String getName(int typeId) {
        switch (typeId) {
            case Types.CAT:
                return "貓";
            case Types.DOG:
                return "狗";
            case Types.ELEPHANT:
                return "象";
            case Types.GOAT:
                return "羊";
            case Types.HORSE:
                return "馬";
            case Types.LION:
                return "獅";
            case Types.MOUSE:
                return "鼠";
            case Types.SPARROW:
                return "雀";
            case Types.TIGER:
                return "虎";
            case Types.WOLF:
                return "狼";
            case Types.RIVER:
                return "河";
            default:
                throw new IllegalArgumentException("No name of type=" + typeId + " found.");
        }
    }

    private static final Map<Integer, Sprite> spriteMap = new HashMap<>();

    static {
        // animals
        spriteMap.put(Types.CAT, new SoundSprite(Types.CAT, DEFAULT_VOLUME,
                new ProbablyVolumeWaveTriggerBehavior(0.005, new LinearVolumeBehavior(0.004))));
        spriteMap.put(Types.DOG, new SoundSprite(Types.DOG, DEFAULT_VOLUME,
                new ProbablyVolumeWaveTriggerBehavior(0.0064, new LinearVolumeBehavior(0.01))));
        spriteMap.put(Types.ELEPHANT, new SoundSprite(Types.ELEPHANT, 0.1,
                new ProbablyVolumeWaveTriggerBehavior(0.00055, new SinVolumeBehavior(10))));
        spriteMap.put(Types.GOAT, new SoundSprite(Types.GOAT, 0.2,
                new ProbablyVolumeWaveTriggerBehavior(0.002032, new SinVolumeBehavior(4))));
        spriteMap.put(Types.HORSE, new SoundSprite(Types.HORSE, 0.2,
                new ProbablyVolumeWaveTriggerBehavior(0.00341, new SinVolumeBehavior(4))));
        spriteMap.put(Types.LION, new SoundSprite(Types.LION, 0.1,
                new ProbablyVolumeWaveTriggerBehavior(0.0009, new SinVolumeBehavior(6))));
        spriteMap.put(Types.MOUSE, new SoundSprite(Types.MOUSE, DEFAULT_VOLUME,
                new ProbablyVolumeWaveTriggerBehavior(0.0072, new LinearVolumeBehavior(0.02))));
        spriteMap.put(Types.SPARROW, new SoundSprite(Types.SPARROW, DEFAULT_VOLUME,
                new ProbablyVolumeWaveTriggerBehavior(0.006882, new LinearVolumeBehavior(0.02))));
        spriteMap.put(Types.TIGER, new SoundSprite(Types.TIGER, 0.1,
                new ProbablyVolumeWaveTriggerBehavior(0.00177, new SinVolumeBehavior(8))));
        spriteMap.put(Types.WOLF, new SoundSprite(Types.WOLF, DEFAULT_VOLUME,
                new ProbablyVolumeWaveTriggerBehavior(0.004, new LinearVolumeBehavior(0.009))));
        spriteMap.put(Types.RIVER, new SoundSprite(Types.RIVER, DEFAULT_VOLUME,
                new SinVolumeBehavior(20)));
    }

    public static Sprite createSprite(int typeId) {
        if (!spriteMap.containsKey(typeId)) {
            throw new IllegalArgumentException("TypeId " + typeId + " not found.");
        }
        return spriteMap.get(typeId).clone();
    }


    public static Sprite createSprite(int typeId, Vector3 initPoint) {
        Sprite s = createSprite(typeId);
        s.setPoint(initPoint);
        return s;
    }
}
