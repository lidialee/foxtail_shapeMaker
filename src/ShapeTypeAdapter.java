import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class ShapeTypeAdapter extends TypeAdapter<Shape>{
    // // .java ---> write() ---> .json
    @Override
    public void write(JsonWriter jsonWriter, Shape shape) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("shapeType").value(shape.getClass().getName());
        jsonWriter.name("x").value(shape.getPoint().getX());
        jsonWriter.name("y").value(shape.getPoint().getY());


    }



    // .json ---> read() ---> .java
    @Override
    public Shape read(JsonReader jsonReader) throws IOException {
        return null;
    }
}
