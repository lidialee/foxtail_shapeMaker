import java.io.Serializable;

public class Point implements Serializable{
    private float x;
    private float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // getter
    public float getX() { return x; }
    public float getY() {
        return y;
    }

    // setter
    public void setX(float x) {
        this.x = x;
    }
    public void setY(float y) {
        this.y = y;
    }
}
