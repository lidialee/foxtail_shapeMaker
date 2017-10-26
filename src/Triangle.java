import processing.core.PApplet;

/**
 * 주의사항
 * 모든 것은 '정삼각형'으로 그려지고 계산되도록 함수를 작성하였다
 * 마우스 클릭으로 부터 저장되는 x/y == 정삼각형의 내심(=외심=무게중심)
 * draw(), collsion() 모두 정삼각형 내심(=외심=무게중심)을 이용한 1:sqru(3):2의 비율을 활용해 해결함
 * <p>
 * 이해가 잘 안되면 정삼각형과 그 외접원 그림을 보면 된다
 */

public class Triangle extends Shape {
    private static float SHORTEST = 30;   // 내심에서 삼각형 밑변까지 수직길이
    private Point[] otherPoints;

    public Triangle() {
        super(3);
        // 여기서 otherPoints에 생성자를 할당하면 clone()할때
        // otherPoints는 shallow copy되므로
        // setPoint()를 통해 new Point()를 할당 받고 나서
        // 새롭게  otherPoints = new Point[3]; 해주자
    }


    @Override
    public void setPoint(Point point) {
        if(point!=null){
            super.setPoint(point);
            calculateThreePoints(point);
        }else
            System.out.println("입력으로 부터 받아온 point부터가 null");

    }

    // 세 꼭지점 계산하기.
    private void calculateThreePoints(Point point) {
        otherPoints = new Point[3];
        otherPoints[0] = new Point((float) (point.getX() - SHORTEST * Math.sqrt(3)), point.getY() + SHORTEST);
        otherPoints[1] = new Point(point.getX(), point.getY() - SHORTEST * 2);
        otherPoints[2] = new Point((float) (point.getX() + SHORTEST * Math.sqrt(3)),point.getY() + SHORTEST );
    }

    @Override
    void draw(PApplet pApplet) {
        Color color = getColor();
        Point point = getPoint();
        pApplet.fill(color.getR(), color.getG(), color.getB());

        if(isClicked())
            pApplet.stroke(255,255,255);
        else
            pApplet.stroke(0,0,0);


        pApplet.triangle(otherPoints[0].getX(), otherPoints[0].getY(),
                otherPoints[1].getX(), otherPoints[1].getY(),
                otherPoints[2].getX(), otherPoints[2].getY());
    }

    // 잘못된 충돌알고리즘 입니다 : 꼭지점 부근은 충돌로 인정되지 않습니다
    @Override
    boolean isCollision(int x, int y) {
        float deltaX = otherPoints[0].getX() - x;
        float deltaY = otherPoints[0].getY() - y;
        boolean isLong1 = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY) <= SHORTEST * 3;
        //System.out.println("삼각형 왼쪽 아래 꼭지점과 클릭한 점과의 거리 : " + isLong1);

        deltaX = otherPoints[1].getX() - x;
        deltaY = otherPoints[1].getY() - y;
        boolean isLong2 = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY) <= SHORTEST * 3;
        //System.out.println("삼각형 위 꼭지점과 클릭한 점과의 거리 : " + isLong2);

        deltaX = otherPoints[2].getX() - x;
        deltaY = otherPoints[2].getY() - y;
        boolean isLong3 = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY) <= SHORTEST * 3;
        // System.out.println("삼각형 오른쪽 아래꼭지점과 클릭한 점과의 거리: " + isLong3);

        return isLong1 && isLong2 && isLong3;
    }

    @Override
    public Triangle clone() {
        return (Triangle) super.clone();
    }
}
