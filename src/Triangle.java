import processing.core.PApplet;

import java.awt.*;

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
    private float[] B_L_Point;            // 왼쪽 아래 꼭지점
    private float[] B_R_Point;            // 오른쪽 아래 꼭지점
    private float[] T_Point;            // 윗 꼭지점

    public Triangle() {
        super(3);
        B_L_Point = new float[2];
        B_R_Point = new float[2];
        T_Point = new float[2];
    }

    // 삼각형의 3개의 꼭지점을 손쉽게 사용하기 위해
    // 마우스 클릭으로부터 내심인 x,y를 받아오는 동시에
    // 3점의 위치를 계산해서 저장해놓자
    @Override
    public void setPoint(Point point) {
        super.setPoint(point);

        B_L_Point[0] = (float) (point.getX() - SHORTEST * Math.sqrt(3));
        B_L_Point[1] = point.getY() + SHORTEST;

        T_Point[0] = point.getX();
        T_Point[1] = point.getY() - SHORTEST * 2;

        B_R_Point[0] = (float) (point.getX() + SHORTEST * Math.sqrt(3));
        B_R_Point[1] = point.getY() + SHORTEST;
    }

    @Override
    void draw(PApplet pApplet) {
        Color color = getColor();
        Point point = getPoint();

        pApplet.fill(color.getRed(), color.getGreen(), color.getBlue());
        pApplet.triangle(B_L_Point[0], B_L_Point[1], T_Point[0], T_Point[1], B_R_Point[0], B_R_Point[1]);
    }

    // 잘못된 충돌알고리즘 입니다 : 꼭지점 부근은 충돌로 인정되지 않습니다
    @Override
    boolean isCollision(int x, int y) {
        float deltaX = B_L_Point[0] - x;
        float deltaY = B_L_Point[1] - y;
        boolean isLong1 = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY) <= SHORTEST*3;
        //System.out.println("삼각형 왼쪽 아래 꼭지점과 클릭한 점과의 거리 : " + isLong1);

        deltaX = T_Point[0] - x;
        deltaY = T_Point[1] - y;
        boolean isLong2 = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY) <= SHORTEST*3;
        //System.out.println("삼각형 위 꼭지점과 클릭한 점과의 거리 : " + isLong2);

        deltaX = B_R_Point[0] - x;
        deltaY = B_R_Point[1] - y;
        boolean isLong3 = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY) <= SHORTEST*3;
        // System.out.println("삼각형 오른쪽 아래꼭지점과 클릭한 점과의 거리: " + isLong3);

        return isLong1 && isLong2 && isLong3;
    }

    @Override
    public Triangle clone() {
        return (Triangle) super.clone();
    }
}
