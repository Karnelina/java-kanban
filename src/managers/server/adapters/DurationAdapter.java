package managers.server.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationAdapter extends TypeAdapter<Duration> {
    @Override
    public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
        try {
            jsonWriter.value(duration.toMinutes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Duration read(JsonReader jsonReader) throws IOException {
        try {
            return Duration.ofMinutes(jsonReader.nextLong());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
