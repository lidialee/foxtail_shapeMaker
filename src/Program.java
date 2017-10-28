import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import processing.core.PApplet;

import java.io.*;
import java.lang.reflect.Type;
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
                makeShape(1);
                status = MODE_STATUS.CREATE;
            } else if (keyCode == '2') {
                makeShape(2);
                status = MODE_STATUS.CREATE;
            } else if (keyCode == '3') {
                makeShape(3);
                status = MODE_STATUS.CREATE;
            } else if (keyCode == 'c' || keyCode == 'C') {
                if (list.size() > 0) {
                    System.out.println("click one Shape that you want to copy");
                    status = MODE_STATUS.COPY;
                } else {
                    System.out.println("nothing to copy");
                }
            } else if (keyCode == 's' || keyCode == 'S') {
                saveShapes();
            } else if (keyCode == 'l' || keyCode == 'L') {
                loadShapes();
            } else if (keyCode == 'r' || keyCode == 'R') {
                clearBoard();
            } else if (keyCode == 'a' || keyCode == 'A') {
                System.out.println("현재 도형수 : " + list.size());
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
        List<Integer> numlist = new ArrayList<>();
        for (Shape shape : list) {
            if (shape.isCollision(mouseX, mouseY))
                numlist.add(num);
            num++;
        }

        if(numlist.size()==0)
            return NO_COLLISION;
        else
            return numlist.get(numlist.size()-1);
    }

    @Override
    public void mousePressed() {
        int collisionShapeNumber = checkCollision();
        Shape copyTarget = null;

        switch (status) {
            case CREATE:
                if (collisionShapeNumber == NO_COLLISION && newShape != null) {
                    newShape.setPoint(new Point(mouseX, mouseY));
                    list.add(newShape);
                    newShape = null;

                    status = MODE_STATUS.MOVE;
                }else{
                    System.out.println("collision!!! please click another place");
                }
                break;

            case COPY:
                if (collisionShapeNumber != NO_COLLISION) {
                    copyTarget = list.get(collisionShapeNumber);
                    newShape = copyTarget.clone();
                    list.add(newShape);
                    newShape = null;

                    status = MODE_STATUS.MOVE;
                } else
                    System.out.println("There is no Shape. please click the shape what you wanna copy");

                break;

            case MOVE:
                if (newShape == null && collisionShapeNumber != NO_COLLISION) {
                    choiceShape = list.get(collisionShapeNumber); // 선택한 도형 목록에서 가져오기
                    choiceShape.setClicked(true);
                }
                break;
        }
    }

    @Override
    public void mouseDragged() {
        if (choiceShape != null) {
            choiceShape.setPoint(new Point(mouseX, mouseY)); // 드래그 될때마다 위치값을 바꾼다
        }
    }

    @Override
    public void mouseReleased() {
        if (choiceShape != null) {
            choiceShape.setClicked(false);
            choiceShape = null;
        }
    }


    private void saveShapes() {
        try( BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("jsonFile.json")) ){
            Gson gson = new GsonBuilder()
                    .registerTypeHierarchyAdapter(Shape.class, new ShapeTypeAdapter())
                    .create();

            String savedJson = gson.toJson(list);
            bufferedWriter.write(savedJson);
            System.out.println("저장 완료");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 이전 그림 불러오기
    private void loadShapes() {
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader("jsonFile.json"))){
            list.clear();   // 먼저 기존을 삭제하고 불러오자

            Gson gson = new GsonBuilder()
                    .registerTypeHierarchyAdapter(Shape.class, new ShapeTypeAdapter())
                    .create();
            Type type = new TypeToken<List<Shape>>(){}.getType();
            list = gson.fromJson(bufferedReader,type);
            System.out.println("불러오기 완료");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 완전 초기화 함수.
    private void clearBoard() {
        list.clear();
        background(255);
        System.out.println("클리어 완료");
    }


    private void makeShape(int shapeType) {
        if (shapeType == 1) {
            System.out.println("you choose square. click where you want");
            newShape = new Rect();
        } else if (shapeType == 2) {
            System.out.println("you choose circle. click where you want");
            newShape = new Circle();
        } else if (shapeType == 3) {
            System.out.println("you choose triangle. click where you want");
            newShape = new Triangle();
        }
    }

}

// 오늘의 개념
// 객체 -->  바이트 스트림으로 바꾸는 것 직렬화 ( = serialization, 먀살링 )
// serializable interface = markup interface ( Cloneable과 같네  )
// --> 왠만해서 이런 직렬은 안좋다, 왜? 불필요한것 까지 저장되기 때문
// --> 저장되지 않아도 되는걸 transient로 설정하는 것 좋다
// 반대 개념 --> 역직렬화 ( = deserialization , 언마샬링 )

// for 문을 사용하지 않고 그냥 한줄로 끝낼 수 있다
// java.awt 라이브러리의 Color 객체를 사용하지 않는 것이 좋다
// 저 라이브러리에 종속성이 생긴다
// 꼭 color를 따로 만들자

// 텍스트로 저장하는 것이 좋지 --> json 형식으로 저장하는 것도 심플하고 좋다  // Gson
// 다른 기종간의 쉬운 의사 전달을 위해 텍스트로 하는게 제이슨
// 다른 기종간 바이너리 파일 전달을 위한 것이 google protocol buffer (GERPC) RAM = RPC
// 자바 컨벤션 -- camel 표기법

// try with resource;

// 숙제 : json을 통해 저장
// google의 프로토 버퍼 사용해서 해보기
// 여기도transient로된다


