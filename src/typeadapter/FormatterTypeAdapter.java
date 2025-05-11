package typeadapter;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormatterTypeAdapter implements JsonSerializer<DateTimeFormatter>, JsonDeserializer<DateTimeFormatter> {

    @Override
    public JsonElement serialize(final DateTimeFormatter formatter, final Type typeOfSrc,
                                 final JsonSerializationContext context) {
        return new JsonPrimitive(formatter.toString());
    }

    @Override
    public DateTimeFormatter deserialize(final JsonElement json, final Type typeOfT,
                                     final JsonDeserializationContext context) throws JsonParseException {
        // Да, это костыль
        return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    }
}