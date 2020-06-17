package server.protocol;

import world.Sprite;

import java.util.Collection;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public interface Protocol {

    byte[] packInitializedSprites(Collection<Sprite> sprites);

    int getAnimalTypeCode(Sprite sprite);

    UpdateLocationRequest parseUpdateLocationRequest(byte[] packet);

    class UpdateLocationRequest {
        public double x, y, z;
        public int angle;

        public UpdateLocationRequest(double x, double y, double z, int angle) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.angle = angle;
        }
    }
}
