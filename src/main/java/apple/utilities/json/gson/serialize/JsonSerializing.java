package apple.utilities.json.gson.serialize;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

public interface JsonSerializing<T> extends JsonSerializer<T>, JsonDeserializer<T> {

}
