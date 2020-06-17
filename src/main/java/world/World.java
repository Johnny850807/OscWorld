package world;

import java.util.Collection;
import java.util.HashSet;

public class World {
    // a set of sprites excluding the player
    private Collection<Sprite> sprites = new HashSet<>();
    private Player player = new Player();

    public World() {
        player.setPoint(new Vector3(0, 0, 0));
    }

    public void addSprite(Sprite sprite) {
        sprites.add(sprite);
    }

    public void updatePlayerLocation(Vector3 point, double angle) {
        player.setPoint(point);
        player.setAngle(angle);
    }

    public Collection<Sprite> getSprites() {
        return sprites;
    }

    public Player getPlayer() {
        return player;
    }
}
