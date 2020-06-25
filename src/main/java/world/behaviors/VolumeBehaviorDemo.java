package world.behaviors;

import world.SoundSprite;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class VolumeBehaviorDemo {
    public static void main(String[] args) {
        final double PROBABILITY = 0.7;
        ProbablyUpdateBehavior behavior = new ProbablyUpdateBehavior(PROBABILITY,
                new SinVolumeBehavior(1.7));
        SoundSprite sprite = new SoundSprite(1, 0, behavior);

        for (int i = 0; i < 10000; i++) {
            sprite.update();
            System.out.println(sprite.getVolume());
        }
        System.out.println("Ratio: " + behavior.getOccurrenceRatio());
    }
}
