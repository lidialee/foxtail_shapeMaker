import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class ShapeTypeAdapter extends TypeAdapter<Shape>{
    //  .java ---> write() ---> .json
    @Override
    public void write(JsonWriter jsonWriter, Shape shape) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("shapeType").value(shape.getClass().getName());
        jsonWriter.name("x").value(shape.getPoint().getX());
        jsonWriter.name("y").value(shape.getPoint().getY());
        jsonWriter.name("R").value(shape.getColor().getR());
        jsonWriter.name("G").value(shape.getColor().getG());
        jsonWriter.name("B").value(shape.getColor().getB());
        jsonWriter.endObject();
    }



    // .json ---> read() ---> .java
    @Override
    public Shape read(JsonReader jsonReader) throws IOException {

        jsonReader.beginObject();
        Shape s = null;
        float pointX = 0,pointY=0;
        int colorR=0,colorG=0,colorB=0;

        while(jsonReader.hasNext()){
            switch (jsonReader.nextName()){
                case "shapeType":
                    String type = jsonReader.nextString();

                    if(type.equals("Rect"))
                        s = new Rect();
                    else if(type.equals("Circle"))
                        s = new Circle();
                    else if(type.equals("Triangle"))
                        s = new Triangle();
                    break;
                case "x":
                    if(s!=null)
                        pointX = jsonReader.nextInt();
                    break;
                case "y":
                    if(s!=null)
                        pointY = jsonReader.nextInt();
                    break;
                case "R":
                    if(s!=null)
                        colorR = jsonReader.nextInt();
                    break;
                case "G":
                    if(s!=null)
                        colorG = jsonReader.nextInt();
                    break;
                case "B":
                    if(s!=null)
                        colorB = jsonReader.nextInt();
                    break;
            }
        }

        assert s != null;
        s.setPoint(new Point(pointX,pointY));
        s.setColor(new Color(colorR,colorG,colorB));

        jsonReader.endObject();
        return s;
    }
}
