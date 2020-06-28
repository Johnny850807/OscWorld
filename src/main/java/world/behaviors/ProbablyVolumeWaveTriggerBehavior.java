package world.behaviors;

import world.SoundSprite;
import world.Sprite;

import java.util.Random;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class ProbablyVolumeWaveTriggerBehavior implements Sprite.Behavior {
    private Random random = new Random(new Random().nextLong());
    private WaveVolumeBehavior delegate;
    private double probability;
    private boolean triggering = false;
    private double startTriggerVolume;
    private long latestCheckTime;

    public ProbablyVolumeWaveTriggerBehavior(double probability,
                                             WaveVolumeBehavior delegate) {
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
            if (soundSprite.getVolume() <= 0.1) {
                triggering = false;
                startTriggerVolume = -1;
            }
        } else if (System.currentTimeMillis() - latestCheckTime >= 3000){
            latestCheckTime = System.currentTimeMillis() +
                    (random.nextInt(2000)) /*increase randomness*/;
            double prob = random.nextDouble()*100;
            if (prob <= probability*100) {
                triggering = true;
                startTriggerVolume = soundSprite.getVolume();
                delegate.startWave();
            }
        }
    }

    @Override
    public ProbablyVolumeWaveTriggerBehavior clone() {
        try {
            ProbablyVolumeWaveTriggerBehavior clone = (ProbablyVolumeWaveTriggerBehavior) super.clone();
            clone.delegate = (WaveVolumeBehavior) this.delegate.clone();
            clone.random = new Random(new Random().nextLong());
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
