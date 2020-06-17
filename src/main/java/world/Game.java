package world;

import java.util.Collection;

public class Game {
	private World world;

	public void initializeAndStart() {

	}

	public Collection<Sprite> getSprites() {
		return world.getSprites();
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}
}
