import java.util.Map;

public class LookupFactory {
    public static final LookupFactory INSTANCE = new LookupFactory();

    private LookupFactory() {
    }

    public Lookup mapLookup(Map<String, ?> map) {
        return new MapLookup(map);
    }

    public Lookup environmentLookup() {
        return new EnvironmentLookup();
    }

    public Lookup systemPropertyLookup() {
        return new SystemPropertyLookup();
    }

    public Lookup interpolatorLookup() {
        return new InterpolatorLookup();
    }

    public <V> Lookup interpolatorLookup(Map<String, V> defaultMap) {
        return new InterpolatorLookup(defaultMap);
    }

    public Lookup interpolatorLookup(Lookup defaultLookup) {
        return new InterpolatorLookup(defaultLookup);
    }
}
