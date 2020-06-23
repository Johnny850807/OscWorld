package server.protocol;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import world.Bird;
import world.Sprite;
import world.Vector3;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class V1Protocol implements Protocol {
    private static Logger logger = LogManager.getLogger(V1Protocol.class);
    private final static byte START_GAME_OPCODE = (byte) 0x9D;
    private final static byte UPDATE_LOCATION_OPCODE = (byte) 0x9B;
    private final static byte PLAY_SOUND_OPCODE = (byte) 0x9C;
    private final static byte GAME_OVER_OPCODE = (byte) 0x9E;

    @Override
    public int getAnimalTypeCode(Sprite sprite) {
        if (sprite instanceof Bird) {
            return 1;
        }
        throw new IllegalArgumentException("Animal Type not exists for the class " + sprite.getClass() + ".");
    }

    @Override
    public void handleNextRequest(InputStream in, RequestHandler requestHandler) throws IOException {
        byte opCode = (byte) in.read();
        switch (opCode) {
            case UPDATE_LOCATION_OPCODE:
                handleUpdateLocationRequest(in, requestHandler);
                break;
            case PLAY_SOUND_OPCODE:
                handlePlaySoundRequest(in, requestHandler);
                break;
            case START_GAME_OPCODE:
                logger.debug("[StartGameRequest]");
                requestHandler.onStartGameRequest();
                break;
            case GAME_OVER_OPCODE:
                logger.debug("[GameOverRequest]");
                requestHandler.onGameOverRequest();
                break;
            default:
                throw new IllegalStateException("Illegal opCode " + opCode);
        }
    }

    private void handleUpdateLocationRequest(InputStream in, RequestHandler requestHandler) throws IOException {
        int contentLength = readInt(in);
        byte[] contentBytes = new byte[contentLength];
        for (int i = 0; i < contentLength; i++) {
            contentBytes[i] = (byte) in.read();
        }

        String content = new String(contentBytes, StandardCharsets.US_ASCII);
        String[] split = content.split(",");

        logger.debug("[UpdateLocationRequest: read] content-length: {}, raw: {}", contentBytes.length, content);

        requestHandler.onUpdateLocationRequest(new UpdateLocationRequest(
                new Vector3(Double.parseDouble(split[0]),
                        Double.parseDouble(split[1]),
                        Double.parseDouble(split[2])),
                Double.parseDouble(split[3])));
    }

    private void handlePlaySoundRequest(InputStream in, RequestHandler requestHandler) throws IOException {
        int soundId = readInt(in);
        logger.debug("[PlaySoundRequest: read] soundId: {}", soundId);
        requestHandler.onPlaySoundRequest(new PlaySoundRequest(soundId));
    }


    @Override
    public void writeInitializedSprites(OutputStream out, Collection<Sprite> sprites) throws IOException {
        // generate content
        StringBuilder stringBuilder = new StringBuilder();
        for (Sprite sprite : sprites) {
            stringBuilder.append(getAnimalTypeCode(sprite)).append(",")
                    .append(sprite.getPoint().x).append(",")
                    .append(sprite.getPoint().y).append(",")
                    .append(sprite.getPoint().z).append("\n");
        }
        // delete the final '\n' break-line
        stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());

        // pack content into bytes
        String content = stringBuilder.toString();
        byte[] contentBytes = content.getBytes(StandardCharsets.US_ASCII);

        logger.debug("[InitializedSprites: write] content-length: {}, raw: {}", contentBytes.length, content);

        // protocol
        try {
            out.write(START_GAME_OPCODE);  // OpCode
            writeInt(out, contentBytes.length);
            out.write(contentBytes);
        } catch (IOException ignored) {
        }
    }

    private static void writeInt(OutputStream out, int v) throws IOException {
        out.write((v >>> 24) & 0xFF);
        out.write((v >>> 16) & 0xFF);
        out.write((v >>> 8) & 0xFF);
        out.write((v & 0xFF));
    }

    private static int readInt(InputStream in) throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        int ch3 = in.read();
        int ch4 = in.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4));
    }

}
