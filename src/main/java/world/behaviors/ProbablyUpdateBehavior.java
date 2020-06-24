package world.behaviors;

import world.Sprite;

import java.util.Random;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class ProbablyUpdateBehavior implements Sprite.Behavior {
    private Random random = new Random(System.currentTimeMillis());
    private Sprite.Behavior delegate;
    private double probability;
    private int updateCount = 0;
    private int occurrence = 0;

    public ProbablyUpdateBehavior(double probability, Sprite.Behavior delegate) {
        this.delegate = delegate;
        this.probability = probability;
    }

    @Override
    public void onUpdate(Sprite sprite) {
        updateCount++;
        if (random.nextInt(100) / 100.0 <= probability) {
            occurrence++;
            delegate.onUpdate(sprite);
        }
    }

    public double getOccurrenceRatio() {
        return (double) occurrence / updateCount;
    }
}
