package world.behaviors;

import world.SoundSprite;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class LinearVolumeBehavior extends VolumeBehavior implements WaveVolumeBehavior {
    private boolean increase = true;
    private double interval;
    private double floor;
    private double ceiling;

    public LinearVolumeBehavior() {
        this(0.03);
    }

    public LinearVolumeBehavior(double interval) {
        this(interval, 0, 1);
    }

    public LinearVolumeBehavior(double interval, double floor, double ceiling) {
        this.interval = interval;
        this.floor = floor;
        this.ceiling = ceiling;
    }

    @Override
    protected double onNewVolume(double x, double oldVolume) {
        if (oldVolume <= floor) {
            increase = true;
        } else if (oldVolume >= ceiling) {
            increase = false;
        }

        if (increase) {
            return Math.min(oldVolume+interval, 1);
        } else {
            return Math.max(oldVolume-interval, 0);
        }
    }

    public static void main(String[] args) {
        SoundSprite sprite = new SoundSprite(1, 0.4, new LinearVolumeBehavior(0.005));
        for (int i = 0; i < 10000; i++) {
            sprite.update();
            System.out.println(sprite.getVolume());
        }
    }

    @Override
    public void startWave() {
        increase = true;
    }
}
