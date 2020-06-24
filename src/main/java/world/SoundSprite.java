package world;

import server.MockOscAdapter;
import server.OscAdapter;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class SoundSprite extends Sprite {
    private double volume = 1;  // 0~1
    private int soundId;
    private OscAdapter oscAdapter = new MockOscAdapter();

    public SoundSprite(int typeId, double volume, int soundId) {
        super(typeId);
        this.volume = volume;
        this.soundId = soundId;
    }

    public SoundSprite(int typeId, double volume, int soundId, Behavior behavior) {
        super(typeId, behavior);
        this.volume = volume;
        this.soundId = soundId;
    }

    public void setOscAdapter(OscAdapter oscAdapter) {
        this.oscAdapter = oscAdapter;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getVolume() {
        return volume;
    }

    public int getSoundId() {
        return soundId;
    }
}
