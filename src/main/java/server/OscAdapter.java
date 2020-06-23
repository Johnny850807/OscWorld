package server;

import world.SoundSprite;

import java.util.List;

public interface OscAdapter {
    void updateSoundTrack(List<? extends SoundSprite> soundSprites);
    void playSound(int soundId);
    void clearAll();
}
