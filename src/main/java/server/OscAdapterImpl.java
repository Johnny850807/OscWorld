package server;

import com.illposed.osc.*;
import com.illposed.osc.transport.udp.OSCPort;
import com.illposed.osc.transport.udp.OSCPortIn;
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
                    "/sounds/" + soundSprite.getTypeId(),
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
        for (int typeId : SoundSprites.Types.getAllAnimals()) {
            OSCMessage msg = new OSCMessage("/sounds/" + typeId, Arrays.asList(0, 0, 0, 0));
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

    public static void main(String[] args) throws IOException {
//        new Thread(() -> {
//            try {
//                OSCPortIn in = new OSCPortIn(9000);
//                in.run();
//                in.addPacketListener(new OSCPacketListener() {
//                    @Override
//                    public void handlePacket(OSCPacketEvent oscPacketEvent) {
//                        System.out.println(oscPacketEvent);
//                    }
//
//                    @Override
//                    public void handleBadData(OSCBadDataEvent oscBadDataEvent) {
//
//                    }
//                });
//                in.startListening();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).start();
        while(true) {
            OscAdapterImpl oscAdapter = new OscAdapterImpl("192.168.1.108", 9000);
            oscAdapter.send(new OSCMessage("/sounds/1",
                    Arrays.asList(0.4, 0.1, 0.2, 0.3)));
        }
    }
}
