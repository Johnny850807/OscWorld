package server.protocol;

import world.Bird;
import world.Sprite;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class V1Protocol implements Protocol {
    @Override
    public byte[] packInitializedSprites(Collection<Sprite> sprites) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // generate content
        StringBuilder stringBuilder = new StringBuilder();
        for (Sprite sprite : sprites) {
            stringBuilder.append(getAnimalTypeCode(sprite)).append(",")
                    .append(sprite.getPoint().x).append(",")
                    .append(sprite.getPoint().y).append(",")
                    .append(sprite.getPoint().z);
        }

        // pack content into bytes
        String content = stringBuilder.toString();
        byte[] contentBytes = content.getBytes(StandardCharsets.US_ASCII);

        // protocol
        try {
            baos.write(-100);  // OpCode
            writeInt(baos, contentBytes.length);
            baos.write(contentBytes);
        } catch (IOException ignored) { }

        return baos.toByteArray();
    }

    private void writeInt(OutputStream out, int v) throws IOException {
        out.write((v >>> 24) & 0xFF);
        out.write((v >>> 16) & 0xFF);
        out.write((v >>> 8) & 0xFF);
        out.write((v & 0xFF));
    }

    @Override
    public int getAnimalTypeCode(Sprite sprite) {
        if (sprite instanceof Bird) {
            return 1;
        }
        throw new IllegalArgumentException("Animal Type not exists for the class " + sprite.getClass() + ".");
    }

    @Override
    public UpdateLocationRequest parseUpdateLocationRequest(byte[] packet) {
        return null;
    }
}
