import processing.core.PApplet;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Program extends PApplet {
    enum MODE_STATUS {
        CREATE, MOVE, COPY
    }

    private List<Shape> list = new ArrayList<>();
    private static final int NO_COLLISION = 2147483647;
    private Shape newShape = null;
    private Shape choiceShape = null;
    private MODE_STATUS status = MODE_STATUS.MOVE;
    private boolean isCtrl = false;

    public static void main(String[] args) {
        PApplet.main("Program");
    }

    @Override
    public void keyPressed() {
        if (keyCode == CONTROL) {
            isCtrl = true;
        }

        if (isCtrl) {
            if (keyCode == '1') {
                makeShape("square");
                status = MODE_STATUS.CREATE;
            } else if (keyCode == '2') {
                makeShape("circle");
                status = MODE_STATUS.CREATE;
            } else if (keyCode == '3') {
                makeShape("triangle");
                status = MODE_STATUS.CREATE;
            } else if (keyCode == 'c' || keyCode == 'C') {
                if (list.size() > 0) {
                    System.out.println("click one Shape that you want to copy");
                    status = MODE_STATUS.COPY;
                }else{
                    System.out.println("nothing to copy");
                }
            } else if (keyCode == 's' || keyCode == 'S') {
                saveShapes();
            } else if (keyCode == 'l' || keyCode == 'L') {
                loadShapes();
            } else if (keyCode == 'r' || keyCode == 'R') {
                clearBoard();
            } else if (keyCode == 'a' || keyCode == 'A') {
                System.out.println("현재 도형수 : "+list.size());
            }
        }
    }

    @Override
    public void keyReleased() {
        if (keyCode == CONTROL) {
            isCtrl = false;
        }
    }

    @Override
    public void draw() {
        background(255);  // 이전 그림을 지우기 위해서
        for (Shape e : list)
            e.draw(this);
    }

    @Override
    public void settings() {
        size(1000, 700);
    }

    @Override
    public void setup() {
        background(255);
        System.out.println("********************************************************************************");
        System.out.println("CONTROL+1 사각형 / CONTROL+2  원 / CONTROL+3 삼각형 / CONTROL+c 복사\nCONTROL+s 저장 / CONTROL+l 불러오기 / CONTROL+r 완전 삭제 / CONTROL+a 현재 그려진 도형 개수");
        System.out.println("********************************************************************************");

    }

    /**
     * 충돌 검사 함수
     * return == num : 도형리스트 중에서 마우스와 충돌한 도형의 번호
     * return == NO_COLLISION : 충돌한 도형이 없다
     * 참고] NO_COLLISION = int의 최대 크기인 2147483647, 이만큼의 도형이 있을 것 같진 않아서 임시방편으로 했습니다
     */
    private int checkCollision() {
        int num = 0;
        for (Shape shape : list) {
            if (shape.isCollision(mouseX, mouseY))
                return num;
            num++;
        }
        return NO_COLLISION;
    }

    @Override
    public void mousePressed() {
        int collisionShapeNumber = checkCollision();
        System.out.println("상태 : "+status+" 충돌은 "+collisionShapeNumber);
        Shape copyTarget = null;

        switch (status) {
            case CREATE:
                if (collisionShapeNumber == NO_COLLISION && newShape != null) {
                    newShape.setPoint(new Point(mouseX, mouseY));
                    list.add(newShape);
                    newShape = null;
                    status = MODE_STATUS.MOVE;
                }
                break;

            case COPY:
                if(collisionShapeNumber!=NO_COLLISION){
                    copyTarget = list.get(collisionShapeNumber);
                    Point newShapePoint = new Point(copyTarget.getPoint().getX() + 20, copyTarget.getPoint().getY() + 20);

                    newShape = copyTarget.clone();
                    newShape.setPoint(newShapePoint);
                    list.add(newShape);
                    newShape = null;

                    status = MODE_STATUS.MOVE;
                }else
                    System.out.println("There is no Shape. please click the shape what you wanna copy");

                break;

            case MOVE:
                if (newShape == null && collisionShapeNumber != NO_COLLISION) {
                    System.out.println("케이스 Move 진입");
                    choiceShape = list.get(collisionShapeNumber); // 선택한 도형 목록에서 가져오기
                    choiceShape.setColor(Color.PINK); // 드래그 중에는 색이 변한다
                }
                break;
        }

    }

    @Override
    public void mouseDragged() {
        if (choiceShape != null) {
            System.out.println("드래그 진입");
            choiceShape.setPoint(new Point(mouseX, mouseY)); // 드래그 될때마다 위치값을 바꾼다
        }
    }

    @Override
    public void mouseReleased() {
        if (choiceShape != null) {
            choiceShape.setColor(Color.darkGray); // 드래그 완료되면 원래 색으로 되돌리기
            choiceShape = null;
        }
    }

    // 현재 그림판 상태 저장
    private void saveShapes() {
        try {
            FileOutputStream fileStream = new FileOutputStream("MyGame.ser");
            ObjectOutputStream os = new ObjectOutputStream(fileStream);

            for (Shape e : list)
                os.writeObject(e);
            os.writeObject(null);       // null을 읽으면

            os.close();
            fileStream.close();
            System.out.println("저장 완료");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 이전 그림 불러오기
    private void loadShapes() {
        try {
            FileInputStream fileStream = new FileInputStream("MyGame.ser");
            ObjectInputStream is = new ObjectInputStream(fileStream);
            Shape shape;

            do {
                shape = (Shape) is.readObject();
                if (shape != null) list.add(shape);
            } while (shape != null);

            is.close();
            fileStream.close();
            System.out.println("불러오기 완료");

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 완전 초기화 함수.
    private void clearBoard() {
        list.clear();
        background(255);
        System.out.println("클리어 완료");
    }


    private void makeShape(String shapeType) {
        if (Objects.equals(shapeType, "square")) {
            System.out.println("you choose square. click where you want");
            newShape = new Rect();
        } else if (Objects.equals(shapeType, "circle")) {
            System.out.println("you choose circle. click where you want");
            newShape = new Circle();

        } else if (Objects.equals(shapeType, "triangle")) {
            System.out.println("you choose triangle. click where you want");
            newShape = new Triangle();
        }
    }

}


// 새로운 도형 위치 입력받는 함수
//    @Override
//    public void mouseClicked() {
//        int collisionShapeNumber = checkCollision();
//        if (collisionShapeNumber == NO_COLLISION && newShape != null) {
//            newShape.setPoint(new Point(mouseX, mouseY));
//            list.add(newShape);
//            newShape = null;
//        }
//    }
//    // 복사하기 함수
//    private void copyShape() {
//        if (list.size() > 0) {
//            System.out.print("몇 번 도형을 복제할지 콘솔창에 입력후 복제할 위치를 클릭해주세요>> ");
//            Scanner scanner = new Scanner(System.in);
//            int num = scanner.nextInt();
//            newShape = list.get(num).clone();
//        } else
//            System.out.println(" 아무것도 그려져 있지 않습니다 ");
//
//    }