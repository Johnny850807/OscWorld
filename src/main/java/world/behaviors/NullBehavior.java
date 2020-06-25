package world.behaviors;

import world.Sprite;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class NullBehavior implements Sprite.Behavior {
    @Override
    public void onUpdate(Sprite sprite) { }

    @Override
    public Sprite.Behavior clone() {
        try {
            return (Sprite.Behavior) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
