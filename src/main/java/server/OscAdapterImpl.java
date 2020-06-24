package server;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCSerializeException;
import com.illposed.osc.transport.udp.OSCPortOut;
import world.SoundSprite;
import world.SoundSprites;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class OscAdapterImpl implements OscAdapter {
    private OSCPortOut oscPortOut;

    public OscAdapterImpl(String host, int port) throws IOException {
        oscPortOut = new OSCPortOut(InetAddress.getByName(host), port);
    }

    @Override
    public void updateSoundTrack(List<? extends SoundSprite> soundSprites) {
        for (SoundSprite soundSprite : soundSprites) {
            OSCMessage msg = new OSCMessage(
                    "/sounds/" + soundSprite.getSoundId(),
                    Arrays.asList(soundSprite.getVolume(),
                            soundSprite.getPoint().x, soundSprite.getPoint().y, soundSprite.getPoint().z));

            send(msg);
        }
    }

    @Override
    public void playSound(int soundId) {
        send(new OSCMessage("/sounds/" + soundId + "/play"));
    }

    @Override
    public void clearAll() {
        for (int soundId : SoundSprites.SoundIDs.getAll()) {
            OSCMessage msg = new OSCMessage("/sounds/" + soundId, Arrays.asList(0, 0, 0, 0));
            send(msg);
        }
    }

    private void send(OSCMessage message) {
        try {
            oscPortOut.send(message);
        } catch (IOException | OSCSerializeException e) {
            e.printStackTrace();
        }
    }
}
