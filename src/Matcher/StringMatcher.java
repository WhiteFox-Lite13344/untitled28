package Matcher;

public interface StringMatcher {
    int isMatch(char[] buffer, int start, int bufferStart, int bufferEnd);

    default int isMatch(String buffer, int start, int bufferStart, int bufferEnd) {
        return isMatch(buffer.toCharArray(), start, bufferStart, bufferEnd);
    }
}
