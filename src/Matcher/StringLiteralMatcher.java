package Matcher;

public class StringLiteralMatcher extends AbstractStringMatcher{
    private final String str;

    StringLiteralMatcher(String str) {
        this.str = str;
    }

    @Override
    public int isMatch(char[] buffer, int start, int bufferStart, int bufferEnd) {
        if (start + str.length() > bufferEnd) {
            return 0;
        }
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != buffer[start + i]) {
                return 0;
            }
        }
        return str.length();
    }

    @Override
    public int isMatch(String buffer, int start, int bufferStart, int bufferEnd) {
        if (start + str.length() > bufferEnd) {
            return 0;
        }
        return buffer.startsWith(str, start) ? str.length() : 0;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + str + "]";
    }
}
