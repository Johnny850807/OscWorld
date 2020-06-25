package world;

import world.behaviors.NullBehavior;

public class Sprite implements Cloneable {
    private int typeId;
    private Vector3 point = new Vector3(0, 0, 0);
    private Behavior behavior = new NullBehavior();

    public Sprite(int typeId) {
        this.typeId = typeId;
    }

    public Sprite(int typeId, Behavior behavior) {
        this.typeId = typeId;
        this.behavior = behavior;
    }

    public void update() {
        behavior.onUpdate(this);
    }

    public int getTypeId() {
        return typeId;
    }

    public Vector3 getPoint() {
        return point;
    }

    public void setPoint(Vector3 p) {
        this.point = p;
    }

    public Sprite clone() {
        try {
            Sprite clone = (Sprite) super.clone();
            clone.point = this.point.clone();
            clone.behavior = this.behavior.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public interface Behavior extends Cloneable {
        void onUpdate(Sprite sprite);
        Behavior clone();
    }
}
