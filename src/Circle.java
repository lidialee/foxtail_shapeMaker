import processing.core.PApplet;

public class Circle extends Shape {
    private static float RADIUS = 45;

    public static float getRADIUS() {
        return RADIUS;
    }

    public static void setRADIUS(float RADIUS) {
        Circle.RADIUS = RADIUS;
    }

    public Circle() {
        super(2);
    }

    @Override
    void draw(PApplet pApplet) {
        // 일단 컬러는 임의로 설정
        int[] color = getColor();
        Point point = getPoint();

        pApplet.fill(color[0], color[1], color[2]);
        pApplet.ellipse(point.getX(), point.getY(), RADIUS * 2, RADIUS * 2);
    }

    @Override
    boolean isCollision(int x, int y) {
        Point point = getPoint();
        float deltaX = point.getX() - x;
        float deltaY = point.getY() - y;

        // 원과 반지름 사이의 거리
        return (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY) < RADIUS;
    }

    @Override
    public Circle clone() {
        return (Circle) super.clone();
    }


}
