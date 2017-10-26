import processing.core.PApplet;


public abstract class Shape implements Cloneable {
    private int shapeType;
    private Color color;
    private Point point;
    private transient boolean isClicked = false;

    public int getShapeType() {
        return shapeType;
    }

    public Shape(int shapeType) {
        this.shapeType = shapeType;
        color = makeRandomColor();
    }

    abstract void draw(PApplet pApplet);

    abstract boolean isCollision(int x, int y);     // (클릭한) 점 ~ 도형의 충돌 검사

    /* (Shape) super.clone()은 도형객체만 deep copy가 될 뿐,
    *  도형 객체 속 객체들 (point, color)는 shallow copy가 되서
    *  복사 대상이 된 객체와 복사로 만들어진 객체가 서로 영향을 받게 된다
    *  그래서 point 와 color는 반드시 new를 통해서 새로운 레퍼런스르 참조하도록 하다
    *  */
    @Override
    public Shape clone() {
        try {
            Shape cloneShape = (Shape) super.clone();
            cloneShape.setPoint(new Point(point.getX()+20,point.getY()+20));
            cloneShape.setColor(new Color(color.getR(),color.getG(),color.getB()));
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
