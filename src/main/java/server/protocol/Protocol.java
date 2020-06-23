package server.protocol;

import world.Sprite;
import world.Vector3;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public interface Protocol {

    void writeInitializedSprites(OutputStream out, Collection<Sprite> sprites) throws IOException;

    int getAnimalTypeCode(Sprite sprite);

    void handleNextRequest(InputStream in, RequestHandler requestHandler) throws IOException;

    interface RequestHandler {
        void onUpdateLocationRequest(UpdateLocationRequest updateLocationRequest);
        void onPlaySoundRequest(PlaySoundRequest playSoundRequest);
        void onStartGameRequest();
        void onGameOverRequest();
    }

    class PlaySoundRequest {
        public int soundId;
        public PlaySoundRequest(int soundId) {
            this.soundId = soundId;
        }
    }

    class UpdateLocationRequest {
        public Vector3 point;
        public double angle;

        public UpdateLocationRequest(Vector3 point, double angle) {
            this.point = point;
            this.angle = angle;
        }
    }
}
