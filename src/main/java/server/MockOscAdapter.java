package server;

import world.SoundSprite;

import java.util.List;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class MockOscAdapter implements OscAdapter {
    @Override
    public void updateSoundTrack(List<? extends SoundSprite> soundSprites) { }

    @Override
    public void playSound(int soundId) { }

    @Override
    public void clearAll() { }
}
