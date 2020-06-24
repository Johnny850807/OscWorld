package world;

public class Player extends Sprite {
    private double angle;

    public Player() {
        super(-1, s -> {});
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }
}
