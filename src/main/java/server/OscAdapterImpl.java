package server;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCSerializeException;
import com.illposed.osc.transport.udp.OSCPortOut;
import world.SoundSprite;

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
                    "/sprites/"+soundSprite.getSoundId(),
                    Arrays.asList(soundSprite.getVolume(),
                            soundSprite.getPoint().x, soundSprite.getPoint().y, soundSprite.getPoint().z));
            try {
                oscPortOut.send(msg);
            } catch (IOException | OSCSerializeException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void playSound(int soundId) {
//        try {
////            oscPortOut.send(msg);
//        } catch (IOException | OSCSerializeException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void clearAll() {

    }
}
