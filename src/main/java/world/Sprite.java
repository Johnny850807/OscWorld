package world;

public abstract class Sprite {
    private Vector3 point;

    public abstract void update();

    public Vector3 getPoint() {
        return point;
    }

    public void setPoint(Vector3 p) {
        this.point = p;
    }
}
