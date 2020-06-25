package world;

import java.util.ArrayList;
import java.util.List;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class World {
    // a set of sprites excluding the player
    private List<Sprite> sprites = new ArrayList<>();
    private Player player = new Player();

    public World() {
        player.setPoint(new Vector3(0, 0, 0));
    }

    public void addSprite(Sprite sprite) {
        sprites.add(sprite);
    }

    public void translatePlayerLocation(double x, double z) {
        player.getPoint().translate(x, z);
    }

    public void updatePlayerLocation(Vector3 point, double angle) {
        player.setPoint(point);
        player.setAngle(angle);
    }

    public List<Sprite> getSprites() {
        return sprites;
    }

    public Player getPlayer() {
        return player;
    }
}
