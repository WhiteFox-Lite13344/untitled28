import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class InterpolatorLookup implements Lookup{
    private static final char PREFIX_SEPARATOR = ':';
    private final Map<String, Lookup> lookupMap;
    private final Lookup defaultLookup;

    InterpolatorLookup() {
        this((Map<String, Object>) null);
    }

    <V> InterpolatorLookup(Map<String, V> defaultMap) {
        this(new MapLookup(defaultMap));
    }

    InterpolatorLookup(Lookup defaultLookup) {
        this(Collections.emptyMap(), defaultLookup, true);
    }

    InterpolatorLookup(Map<String, Lookup> lookupMap, Lookup defaultLookup, boolean addDefaultLookups) {
        this.lookupMap = lookupMap != null ? Map.copyOf(lookupMap) : new HashMap<>();
        if (addDefaultLookups) {
            addDefaultLookups(this.lookupMap);
        }
        this.defaultLookup = defaultLookup;
    }

    private void addDefaultLookups(Map<String, Lookup> lookupMap) {
        lookupMap.put("map", new MapLookup(null));
        lookupMap.put("env", new EnvironmentLookup());
        lookupMap.put("sys", new SystemPropertyLookup());
    }

    @Override
    public String lookup(String key) {
        if (key == null) {
            return null;
        }

        int prefixPos = key.indexOf(PREFIX_SEPARATOR);
        if (prefixPos >= 0) {
            String prefix = key.substring(0, prefixPos).toLowerCase();
            String name = key.substring(prefixPos + 1);
            Lookup lookup = lookupMap.get(prefix);
            if (lookup != null) {
                String value = lookup.lookup(name);
                if (value != null) {
                    return value;
                }
            }
            key = name;
        }

        return defaultLookup != null ? defaultLookup.lookup(key) : null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [lookupMap=" + lookupMap + ", defaultLookup=" + defaultLookup + "]";
    }
}
