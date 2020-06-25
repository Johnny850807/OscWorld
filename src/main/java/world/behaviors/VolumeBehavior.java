package world.behaviors;

import world.SoundSprite;
import world.Sprite;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public abstract class VolumeBehavior implements Sprite.Behavior {
    private double x;
    private double xInterval;

    public VolumeBehavior() {
        this(0.1);
    }

    public VolumeBehavior(double xInterval) {
        this.xInterval = xInterval;
    }

    @Override
    public void onUpdate(Sprite sprite) {
        if (!(sprite instanceof SoundSprite)) {
            throw new IllegalArgumentException("Only support sound sprite");
        }
        x = (Double.MAX_VALUE - x) < xInterval ? 0 : x + xInterval;  // avoid overflow
        SoundSprite soundSprite = (SoundSprite) sprite;
        soundSprite.setVolume(onNewVolume(x, soundSprite.getVolume()));
    }

    protected abstract double onNewVolume(double x, double oldVolume);


    @Override
    public VolumeBehavior clone() {
        try {
            return (VolumeBehavior) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
