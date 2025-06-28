package Matcher;

public class CharMatcher extends AbstractStringMatcher{
    private final char ch;

    CharMatcher(final char ch) {
        this.ch = ch;
    }

    @Override
    public int isMatch(char[] buffer, int start, int bufferStart, int bufferEnd) {
        return ch == buffer[start] ? 1 : 0;
    }

    @Override
    public int isMatch(String buffer, int start, int bufferStart, int bufferEnd) {
        return ch == buffer.charAt(start) ? 1 : 0;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "['" + ch + "']";
    }
}

