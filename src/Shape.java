import processing.core.PApplet;

import java.io.Serializable;

public abstract class Shape implements Cloneable,Serializable {
    private int shapeType;
    private int[] color ;    // 배열로 하지 만ㄹ자
    private Point point;

    public Shape(int shapeType) {
        this.shapeType = shapeType;
        color = new int[]{0, 200, 90};
    }

    abstract void draw(PApplet pApplet);
    abstract boolean isCollision(int x, int y);     // (클릭한) 점 ~ 도형의 충돌 검사

    @Override
    public Shape clone()  {
        try {
            return (Shape) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int[] getColor() { return color; }
    public Point getPoint() { return point; }

    // 색변경 혹은 드래그에 의한 위치변경
    public void setColor(int[] color) { this.color = color; }
    public void setPoint(Point point) { this.point = point; }
}
