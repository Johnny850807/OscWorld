package world;

import world.behaviors.NullBehavior;

public class Player extends Sprite {
    private double angle;

    public Player() {
        super(-1, new NullBehavior());
    }

    public void setAngle(double angle) {
        // todo ignore angle
       // this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }
}
