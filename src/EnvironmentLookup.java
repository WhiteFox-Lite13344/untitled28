public class EnvironmentLookup implements Lookup{
    @Override
    public String lookup(String key) {
        return System.getenv(key);
    }

}
