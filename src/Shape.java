import processing.core.PApplet;


public abstract class Shape implements Cloneable {
    private int shapeType;
    private Color color;
    private Point point;
    private transient boolean isClicked = false;

    public Shape(int shapeType) {
        this.shapeType = shapeType;
        color = makeRandomColor();
    }

    abstract void draw(PApplet pApplet);

    abstract boolean isCollision(int x, int y);     // (클릭한) 점 ~ 도형의 충돌 검사


    @Override
    public Shape clone() {
        try {
            Shape cloneShape = (Shape) super.clone();
            cloneShape.setPoint(new Point(this.point.clone().getX()+20,this.point.clone().getY()+20));
            cloneShape.setColor(this.color.clone());
            return cloneShape;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }


    public Color getColor() {
        return color;
    }

    public Point getPoint() {
        return point;
    }


    // 색변경 혹은 드래그에 의한 위치변경
    public void setColor(Color color) {
        this.color = color;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    private Color makeRandomColor() {
        int R = (int) (Math.random() * 255);
        int G = (int) (Math.random() * 255);
        int B = (int) (Math.random() * 255);
        return new Color(R, G, B);
    }


}
