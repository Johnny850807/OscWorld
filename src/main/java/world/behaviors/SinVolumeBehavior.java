package world.behaviors;

import world.SoundSprite;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class SinVolumeBehavior extends VolumeBehavior {
    private double widthScale;

    public SinVolumeBehavior() {
        this(1);
    }

    public SinVolumeBehavior(double widthScale) {
        this.widthScale = widthScale;
    }

    @Override
    protected double onNewVolume(double x, double oldVolume) {
        return ((Math.sin(x/widthScale) + 1)/2);
    }

}
