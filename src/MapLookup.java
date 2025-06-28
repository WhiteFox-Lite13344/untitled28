import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class MapLookup implements Lookup{
    private final Map<String, ?> map;

    MapLookup(final Map<String, ?> map) {
        this.map = map != null ? Map.copyOf(map) : Collections.emptyMap();
    }

    @Override
    public String lookup(final String key) {
        return Objects.toString(map.get(key), null);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [map=" + map + "]";
    }
}
