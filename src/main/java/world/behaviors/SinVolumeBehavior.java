package world.behaviors;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class SinVolumeBehavior extends VolumeBehavior implements WaveVolumeBehavior {
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

    @Override
    public void startWave() {
        setX(0);  // reset x to 0 to start a new SIN wave
    }
}
