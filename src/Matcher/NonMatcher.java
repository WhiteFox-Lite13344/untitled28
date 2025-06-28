package Matcher;

public class NonMatcher extends AbstractStringMatcher{
    @Override
    public int isMatch(char[] buffer, int start, int bufferStart, int bufferEnd) {
        return 0;
    }

    @Override
    public int isMatch(String buffer, int start, int bufferStart, int bufferEnd) {
        return 0;
    }
}
