import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import processing.core.PApplet;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
            } else if (keyCode == 'o' || keyCode == 'O') {

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
                if (collisionShapeNumber != NO_COLLISION) {
                    copyTarget = list.get(collisionShapeNumber);
                    Point newShapePoint = new Point(copyTarget.getPoint().getX() + 20, copyTarget.getPoint().getY() + 20);

                    newShape = copyTarget.clone();
                    newShape.setPoint(newShapePoint);
                    list.add(newShape);
                    newShape = null;

                    status = MODE_STATUS.MOVE;
                } else
                    System.out.println("There is no Shape. please click the shape what you wanna copy");

                break;

            case MOVE:
                if (newShape == null && collisionShapeNumber != NO_COLLISION) {
                    System.out.println("케이스 Move 진입");
                    choiceShape = list.get(collisionShapeNumber); // 선택한 도형 목록에서 가져오기
                    choiceShape.setColor(new Color(100,200,50)); // 드래그 중에는 색이 변한다
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
            choiceShape.setColor(new Color(0,0,0)); // 드래그 완료되면 원래 색으로 되돌리기
            choiceShape = null;
        }
    }

    // 현재 그림판 상태 저장
    //
    private void saveShapes() {
        try{
            Gson gson = new Gson();
            String gsonString = gson.toJson(list);
            FileWriter writer = new FileWriter("filebox.txt");
            writer.write(gsonString);
            writer.close();

            System.out.println(" 저장 완료 ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 이전 그림 불러오기
    private void loadShapes() {
        try {
            list.clear();   // 먼저 기존을 삭제하고 불러오자

            JsonParser parser = new JsonParser();
            parser.parse(new FileReader("filebox.txt"));
            Gson gson = new Gson();


            // 각 도형에 대해서 어떻게 저장할지 그 계층을 정의ㅎ해부자
            //Gson gson1 = new GsonBuilder().registerTypeHierarchyAdapter(Shape.class,new ShapeTypeAdapter).create();


            // Shape shape = gson.fromJson( string ,Shape.class);

            // 일단 list<shape>에서 shape를 알아내는 방법
            // Type type = new TypeToken<List<Shape>>(){}.getType();
            // Shape shape = gson.fromJson(string인 json, type);



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

    private void showProperty() {
        for (Shape e : list) {
            System.out.println(e.getPoint().getX() + " : " + e.getPoint().getY());
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
