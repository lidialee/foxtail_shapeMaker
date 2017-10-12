import processing.core.PApplet;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Program extends PApplet {
    private List<Shape> list = new ArrayList<>();
    private final int[] tempColor = {44, 123, 90};
    private final int[] originalColor = {0, 200, 90};
    private static final int NO_COLLISION = 2147483647;
    private Shape newShape = null;
    private Shape choiceShape = null;
    private boolean isCtrl = false;


    public static void main(String[] args) {
        PApplet.main("Program");
    }

    @Override
    public void keyPressed(){

        int userInput = saveInput();

        switch (userInput) {
            case 1:
                System.out.println("사각형을 선택을 선택하셨습니다. 그리고 싶은 위치를 클릭해주세요");
                newShape = new Rect();
                break;
            case 2:
                System.out.println("원을 선택을 선택하셨습니다. 그리고 싶은 위치를 클릭해주세요");
                newShape = new Circle();
                break;
            case 3:
                System.out.println("삼각형을 선택을 선택하셨습니다. 그리고 싶은 위치를 클릭해주세요");
                newShape = new Triangle();
                break;
            case 4:
                copyShape();
                break;
            case 5:
                saveShapes();
                break;
            case 6:
                loadShapes();
                break;
            case 7:
                clearBoard();
                break;
            case 8:
                System.out.println("현재 list의 크기 : " + list.size());
                break;
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
        size(900, 900);
    }

    @Override
    public void setup() {
        background(255);
        System.out.println("*************************************************************************");
        System.out.println("ctrl+1 사각형 / ctrl+2  원 / ctrl+3 삼각형 / ctrl+c 복사\nctrl+s 저장 / ctrl+l 불러오기 / ctrl+r 완전 삭제 / ctrl+a 현재 그려진 도형 개수");
        System.out.println("*************************************************************************");

    }

    /**
     * 충돌 검사 함수
     *          return == num : 도형리스트 중에서 마우스와 충돌한 도형의 번호
     *          return == NO_COLLISION : 충돌한 도형이 없다
     *          참고] NO_COLLISION = int의 최대 크기인 2147483647, 이만큼의 도형이 있을 것 같진 않아서 임시방편으로 했습니다
     */
    private int checkCollision(List<Shape> shapeList) {
        int num = 0;
        for (Shape shape : shapeList) {
            if (shape.isCollision(mouseX, mouseY)) {
                System.out.println(num + "번 도형에서 충돌");
                return num;
            }
            num++;
        }
        return NO_COLLISION;
    }

    // 새로운 도형 위치 입력받는 함수
    @Override
    public void mouseClicked() {
        int collisionShapeNumber = checkCollision(list);
        if (collisionShapeNumber == NO_COLLISION && newShape != null) {
            newShape.setPoint(new Point(mouseX, mouseY));
            list.add(newShape);
            newShape = null;
        }
    }

    // 도형 옮기기 순서 : press --> drag --> release
    @Override
    public void mousePressed() {
        int collisionShapeNumber = checkCollision(list);
        if (newShape == null && collisionShapeNumber != NO_COLLISION) {
            choiceShape = list.get(collisionShapeNumber); // 선택한 도형 목록에서 가져오기
            choiceShape.setColor(tempColor); // 드래그 중에는 색이 변한다
        }
    }

    @Override
    public void mouseDragged() {
        if (choiceShape != null){
            System.out.println(mouseX+";"+mouseY);
            choiceShape.setPoint(new Point(mouseX, mouseY)); // 드래그 될때마다 위치값을 바꾼다
        }
    }

    @Override
    public void mouseReleased() {
        if (choiceShape != null){
            choiceShape.setColor(originalColor); // 드래그 완료되면 원래 색으로 되돌리기
            choiceShape =null;
        }

    }

    // 현재 그림판 상태 저장
    private void saveShapes() {
            try{
                FileOutputStream fileStream = new FileOutputStream("MyGame.ser");
                ObjectOutputStream os = new ObjectOutputStream(fileStream);

                for (Shape e : list)
                    os.writeObject(e);
                os.writeObject(null);       // null을 읽으면

                os.close();
                System.out.println("저장 완료");

            }catch (IOException e) {
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
            System.out.println("불러오기 완료");

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 완전 초기화 함수.
    private void clearBoard(){
        list.clear();
        background(255);
        System.out.println("클리어 완료");
    }

    // 복사하기 함수
    private void copyShape(){
        if (list.size() > 0) {
            System.out.print("몇 번 도형을 복제할지 콘솔창에 입력후 복제할 위치를 클릭해주세요>> ");
            Scanner scanner = new Scanner(System.in);
            int num = scanner.nextInt();
            newShape = list.get(num).clone();
        } else
            System.out.println(" 아무것도 그려져 있지 않습니다 ");

    }

    private int saveInput(){
        if(keyCode==157)
            isCtrl = true;

        if(isCtrl){
            if(key=='1'){
                isCtrl=false;
                return 1;
            }
            else if(key=='2'){
                isCtrl=false;
                return 2;
            }
            else if(key=='3'){
                isCtrl=false;
                return 3;
            }
            else if(key=='c'){
                isCtrl=false;
                return 4;
            }
            else if(key=='s'){
                isCtrl=false;
                return 5;
            }
            else if(key=='l'){
                isCtrl=false;
                return 6;
            }
            else if(key=='r'){
                isCtrl=false;
                return 7;
            }
            else if(key=='a'){
                isCtrl=false;
                return 8;
            }
        }
        return 100;
    }

}
