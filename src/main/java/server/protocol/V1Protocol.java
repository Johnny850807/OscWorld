package server.protocol;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import world.Bird;
import world.Sprite;
import world.Vector3;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class V1Protocol implements Protocol {
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
        stringBuilder.delete(stringBuilder.length()-1, stringBuilder.length());

        // pack content into bytes
        String content = stringBuilder.toString();
        byte[] contentBytes = content.getBytes(StandardCharsets.US_ASCII);

        // protocol
        try {
            out.write(0x9C);  // OpCode
            writeInt(out, contentBytes.length);
            out.write(contentBytes);
        } catch (IOException ignored) {
        }
    }


    @Override
    public int getAnimalTypeCode(Sprite sprite) {
        if (sprite instanceof Bird) {
            return 1;
        }
        throw new IllegalArgumentException("Animal Type not exists for the class " + sprite.getClass() + ".");
    }

    @Override
    public UpdateLocationRequest parseUpdateLocationRequest(InputStream in) throws IOException {
        byte opCode = (byte) in.read();
        assert opCode == (byte)0x9B : "OpCode mismatch";

        int contentLength = readInt(in);
        byte[] contentBytes = new byte[contentLength];
        assert in.read(contentBytes) == contentLength;

        String content = new String(contentBytes, StandardCharsets.US_ASCII);
        String[] split = content.split(",");

        return new UpdateLocationRequest(
                new Vector3(Double.parseDouble(split[0]),
                        Double.parseDouble(split[1]),
                        Double.parseDouble(split[2])),
                Double.parseDouble(split[3]));
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
