package server;

import world.SoundSprite;

import java.util.List;

public interface OscAdapter {
    void updateSoundTrack(List<? extends SoundSprite> soundSprites);
}
