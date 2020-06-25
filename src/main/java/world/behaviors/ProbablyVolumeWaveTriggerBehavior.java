package world.behaviors;

import world.SoundSprite;
import world.Sprite;

import java.util.Random;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class ProbablyVolumeWaveTriggerBehavior implements Sprite.Behavior {
    private Random random = new Random(new Random().nextLong());
    private Sprite.Behavior delegate;
    private double probability;
    private boolean triggering = false;
    private double startTriggerVolume;

    public ProbablyVolumeWaveTriggerBehavior(double probability, Sprite.Behavior delegate) {
        this.delegate = delegate;
        this.probability = probability;
    }

    @Override
    public void onUpdate(Sprite sprite) {
        if (!(sprite instanceof SoundSprite)) {
            throw new IllegalStateException("Only supported SoundSprite.");
        }
        SoundSprite soundSprite = (SoundSprite) sprite;
        if (triggering) {
            delegate.onUpdate(soundSprite);
            if (soundSprite.getVolume() <= startTriggerVolume) {
                triggering = false;
                startTriggerVolume = -1;
            }
        } else if (random.nextInt(100) / 100.0 <= probability) {
            triggering = true;
            startTriggerVolume = soundSprite.getVolume();
        }
    }

    @Override
    public ProbablyVolumeWaveTriggerBehavior clone() {
        try {
            ProbablyVolumeWaveTriggerBehavior clone = (ProbablyVolumeWaveTriggerBehavior) super.clone();
            clone.delegate = this.delegate.clone();
            clone.random = new Random(new Random().nextLong());
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
