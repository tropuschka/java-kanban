package typeadapter;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DurationTypeAdapter implements JsonSerializer<Duration>, JsonDeserializer<Duration> {
    @Override
    public JsonElement serialize(final Duration duration, final Type typeOfSrc,
                                 final JsonSerializationContext context) {
        return new JsonPrimitive(duration.toString());
    }

    @Override
    public Duration deserialize(final JsonElement json, final Type typeOfT,
                                     final JsonDeserializationContext context) throws JsonParseException {
        String durationString = json.getAsString();
        String durationNumber = durationString.substring(2, durationString.length() - 1);
        return Duration.ofMinutes(Integer.parseInt(durationNumber));
    }
}
