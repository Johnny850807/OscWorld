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
    private final static double DEFAULT_VOLUME = 0.4;

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

        static int[] getAll() {
            return new int[]{
                    CAT, DOG, ELEPHANT, GOAT, HORSE, LION, MOUSE, SPARROW, TIGER, WOLF
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
            default:
                throw new IllegalArgumentException("No name of type="+typeId+ " found.");
        }
    }
    private static final Map<Integer, Sprite> spriteMap = new HashMap<>();

    static {
        spriteMap.put(Types.CAT, new SoundSprite(Types.CAT, DEFAULT_VOLUME,
                new ProbablyVolumeWaveTriggerBehavior(0.5, new LinearVolumeBehavior(0.004))));
        spriteMap.put(Types.DOG, new SoundSprite(Types.DOG, DEFAULT_VOLUME,
                new ProbablyVolumeWaveTriggerBehavior(0.64, new LinearVolumeBehavior(0.01))));
        spriteMap.put(Types.ELEPHANT, new SoundSprite(Types.ELEPHANT, 0.1,
                new ProbablyVolumeWaveTriggerBehavior(0.055,  new SinVolumeBehavior(10))));
        spriteMap.put(Types.GOAT, new SoundSprite(Types.GOAT, 0.2,
                new ProbablyVolumeWaveTriggerBehavior(0.2032, new SinVolumeBehavior(4))));
        spriteMap.put(Types.HORSE, new SoundSprite(Types.HORSE, 0.2,
                new ProbablyVolumeWaveTriggerBehavior(0.341, new SinVolumeBehavior(4))));
        spriteMap.put(Types.LION, new SoundSprite(Types.LION, 0.1,
                new ProbablyVolumeWaveTriggerBehavior(0.09, new SinVolumeBehavior(6))));
        spriteMap.put(Types.MOUSE, new SoundSprite(Types.MOUSE, DEFAULT_VOLUME,
                new ProbablyVolumeWaveTriggerBehavior(0.72, new LinearVolumeBehavior(0.02))));
        spriteMap.put(Types.SPARROW, new SoundSprite(Types.SPARROW, DEFAULT_VOLUME,
                new ProbablyVolumeWaveTriggerBehavior(0.6882, new LinearVolumeBehavior(0.02))));
        spriteMap.put(Types.TIGER, new SoundSprite(Types.TIGER, 0.1,
                new ProbablyVolumeWaveTriggerBehavior(177, new SinVolumeBehavior(8))));
        spriteMap.put(Types.WOLF, new SoundSprite(Types.WOLF, DEFAULT_VOLUME,
                new ProbablyVolumeWaveTriggerBehavior(0.4, new LinearVolumeBehavior(0.009))));
    }

    public static Sprite createSprite(int typeId) {
        return spriteMap.get(typeId).clone();
    }
}
