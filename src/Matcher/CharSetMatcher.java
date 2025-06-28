package Matcher;

import java.util.Arrays;

public class CharSetMatcher extends AbstractStringMatcher{
    private final char[] chars;

    CharSetMatcher(char[] chars) {
        this.chars = chars.clone();
        Arrays.sort(this.chars);
    }

    @Override
    public int isMatch(char[] buffer, int start, int bufferStart, int bufferEnd) {
        return Arrays.binarySearch(chars, buffer[start]) >= 0 ? 1 : 0;
    }

    @Override
    public int isMatch(String buffer, int start, int bufferStart, int bufferEnd) {
        return Arrays.binarySearch(chars, buffer.charAt(start)) >= 0 ? 1 : 0;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + Arrays.toString(chars);
    }
}

