package server;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCSerializeException;
import com.illposed.osc.transport.udp.OSCPortOut;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private final static Logger logger = LogManager.getLogger();
    private OSCPortOut oscPortOut;

    public OscAdapterImpl(String host, int port) throws IOException {
        oscPortOut = new OSCPortOut(InetAddress.getByName(host), port);
        logger.info("OscHost config {}:{}", host, port);
    }

    @Override
    public void updateSoundTrack(List<? extends SoundSprite> soundSprites) {
        for (SoundSprite soundSprite : soundSprites) {
            OSCMessage msg = new OSCMessage(
                    "/sounds/" + soundSprite.getTypeId(),
                    Arrays.asList((float) soundSprite.getVolume(),
                            (float) soundSprite.getPoint().x,
                            //note in OSC it's a x-y  plane, but in Unity it's a x-z plane
                            (float) soundSprite.getPoint().z,
                            (float) soundSprite.getPoint().y));

            send(msg);
        }
    }

    @Override
    public void playSound(int soundId) {
        send(new OSCMessage("/sounds/" + soundId + "/play"));
    }

    @Override
    public void clearAll() {
        try {
            for (int typeId : SoundSprites.Types.getAllAnimals()) {
                OSCMessage msg = new OSCMessage("/sounds/" + typeId, Arrays.asList(0, 0, 0, 0));
                send(msg);
            }
            for (int typeId : SoundSprites.Types.getAllSurroundings()) {
                OSCMessage msg = new OSCMessage("/sounds/" + typeId, Arrays.asList(0, 0, 0, 0));
                send(msg);
            }
        } catch (Exception err) {

        }
    }

    private synchronized void send(OSCMessage message) {
        try {
            oscPortOut.send(message);
        } catch (IOException | OSCSerializeException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
//        new Thread(() -> {
//            try {
//                OSCPortIn in = new OSCPortIn(9001);
//                in.run();
//                in.addPacketListener(new OSCPacketListener() {
//                    @Override
//                    public void handlePacket(OSCPacketEvent oscPacketEvent) {
//                        System.out.println(oscPacketEvent);
//                    }
//
//                    @Override
//                    public void handleBadData(OSCBadDataEvent oscBadDataEvent) {
//                        System.out.println(oscBadDataEvent);
//                    }
//                });
//                in.startListening();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).start();
        OscAdapterImpl oscAdapter = new OscAdapterImpl("192.168.43.163", 9001);

        oscAdapter.send(new OSCMessage("/sounds/1",
                Arrays.asList()));
    }
}
