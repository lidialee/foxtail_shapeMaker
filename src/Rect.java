import processing.core.PApplet;

public class Rect extends Shape {
    private static float WIDE = 45;         // 정사각형 한변의 1/2 길이

    public static float getWIDE() {
        return WIDE;
    }

    public static void setWIDE(float WIDE) {
        Rect.WIDE = WIDE;
    }

    public Rect() {
        super(1);
    }

    @Override
    void draw(PApplet pApplet) {
        int[] color = getColor();
        float shapeX = getPoint().getX();
        float shapeY = getPoint().getY();

        pApplet.fill(color[0], color[1], color[2]);
        pApplet.rect(shapeX - WIDE, shapeY - WIDE, WIDE * 2, WIDE * 2);
    }

    @Override
    boolean isCollision(int x, int y) {
        float shapeX = getPoint().getX();
        float shapeY = getPoint().getY();
        return shapeX + WIDE >= x && shapeX - WIDE <= x && shapeY + WIDE >= y && shapeY - WIDE <= y;
    }

    @Override
    public Rect clone() {
        return (Rect) super.clone();
    }
}
