public class SystemPropertyLookup implements Lookup{
    @Override
    public String lookup(String key) {
        return System.getProperty(key);
    }
}
