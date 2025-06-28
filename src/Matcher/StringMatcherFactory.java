package Matcher;

public class StringMatcherFactory {
    public static final StringMatcherFactory INSTANCE = new StringMatcherFactory();
    private static final NonMatcher NON_MATCHER = new NonMatcher();

    private StringMatcherFactory() {
    }

    public StringMatcher charMatcher(char ch) {
        return new CharMatcher(ch);
    }

    public StringMatcher charSetMatcher(String chars) {
        if (chars == null || chars.isEmpty()) {
            return NON_MATCHER;
        }
        if (chars.length() == 1) {
            return new CharMatcher(chars.charAt(0));
        }
        return new CharSetMatcher(chars.toCharArray());
    }

    public StringMatcher stringMatcher(String str) {
        if (str == null || str.isEmpty()) {
            return NON_MATCHER;
        }
        return new StringLiteralMatcher(str);
    }

    public StringMatcher noneMatcher() {
        return NON_MATCHER;
    }
}
