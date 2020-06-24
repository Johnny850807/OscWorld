package world.behaviors;

import world.SoundSprite;
import world.Sprite;

public class IncrementalVolumeBehavior implements Sprite.Behavior {
    private boolean increase;

    public IncrementalVolumeBehavior() {
        this(true);
    }

    public IncrementalVolumeBehavior(boolean increase) {
        this.increase = increase;
    }

    @Override
    public void onUpdate(Sprite sprite) {
        if (!(sprite instanceof SoundSprite)) {
            throw new IllegalArgumentException("Only support sound sprite");
        }
        SoundSprite soundSprite = (SoundSprite) sprite;

        if (soundSprite.getVolume() <= 0) {
            increase = true;
        }
        if (soundSprite.getVolume() >= 1) {
            increase = false;
        }

        if (increase) {
            soundSprite.setVolume(Math.min(soundSprite.getVolume()+0.03, 1));
        } else {
            soundSprite.setVolume(Math.max(soundSprite.getVolume()-0.03, 0));
        }
    }
}
