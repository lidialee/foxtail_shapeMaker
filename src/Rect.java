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
        Color color = getColor();
        pApplet.fill(color.getR(), color.getG(), color.getB());

        if(isClicked())
            pApplet.stroke(255,255,255);
        else
            pApplet.stroke(0,0,0);

        pApplet.rect(getPoint().getX() - WIDE, getPoint().getY() - WIDE, WIDE * 2, WIDE * 2);
    }

    @Override
    boolean isCollision(int x, int y) {
        return getPoint().getX() + WIDE >= x && getPoint().getX() - WIDE <= x && getPoint().getY() + WIDE >= y && getPoint().getY() - WIDE <= y;
    }

    @Override
    public Rect clone() {
        return (Rect) super.clone();
    }

}
