package world;

import java.util.Collection;
import java.util.HashSet;

public class World {
	// a set of sprites excluding the player
	private Collection<Sprite> sprites = new HashSet<>();
	private Player player;

	public void updatePlayerLocation(Vector3 point, int angle) {
		// TODO buffer to update
	}

	public Collection<Sprite> getSprites() {
		return sprites;
	}

	public Player getPlayer() {
		return player;
	}
}
