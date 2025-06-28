import Matcher.StringMatcher;
import Matcher.StringMatcherFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TemplateProcessor {
    private static final String DEFAULT_VAR_START = "<";
    private static final String DEFAULT_VAR_END = ">";
    private static final char DEFAULT_ESCAPE = '\\';

    private final StringMatcher prefixMatcher;
    private final StringMatcher suffixMatcher;
    private final char escapeChar;
    private final boolean throwOnUndefined;
    private final Lookup lookup;

    public TemplateProcessor(Map<String, ?> valueMap) {
        this(StringMatcherFactory.INSTANCE.stringMatcher(DEFAULT_VAR_START),
                StringMatcherFactory.INSTANCE.stringMatcher(DEFAULT_VAR_END),
                DEFAULT_ESCAPE, false, LookupFactory.INSTANCE.mapLookup(valueMap));
    }

    public TemplateProcessor(Lookup lookup) {
        this(StringMatcherFactory.INSTANCE.stringMatcher(DEFAULT_VAR_START),
                StringMatcherFactory.INSTANCE.stringMatcher(DEFAULT_VAR_END),
                DEFAULT_ESCAPE, false, lookup);
    }

    public TemplateProcessor(StringMatcher prefixMatcher, StringMatcher suffixMatcher,
                             char escapeChar, boolean throwOnUndefined, Lookup lookup) {
        this.prefixMatcher = Objects.requireNonNull(prefixMatcher, "Prefix matcher must not be null");
        this.suffixMatcher = Objects.requireNonNull(suffixMatcher, "Suffix matcher must not be null");
        this.escapeChar = escapeChar;
        this.throwOnUndefined = throwOnUndefined;
        this.lookup = Objects.requireNonNull(lookup, "Lookup must not be null");
    }

    public String replace(String template) {
        if (template == null || template.isEmpty()) {
            return null;
        }

        Map<Integer, Replacement> replacements = new HashMap<>();
        for (int i = 0; i < template.length(); ) {
            if (i > 0 && template.charAt(i - 1) == escapeChar) {
                i++;
                continue;
            }

            int prefixMatch = prefixMatcher.isMatch(template, i, 0, template.length());
            if (prefixMatch == 0) {
                i++;
                continue;
            }

            int suffixIndex = findSuffixIndex(template, i + prefixMatch, suffixMatcher);
            if (suffixIndex == -1) {
                i++;
                continue;
            }

            String varName = template.substring(i + prefixMatch, suffixIndex);
            String value = lookup.lookup(varName);
            if (value == null && throwOnUndefined) {
                throw new IllegalArgumentException("Undefined variable: " + varName);
            }

            if (value == null) {
                value = template.substring(i, suffixIndex + suffixMatcher.isMatch(template, suffixIndex, 0, template.length()));
            }

            replacements.put(i, new Replacement(value, suffixIndex + suffixMatcher.isMatch(template, suffixIndex, 0, template.length()) - i));
            i = suffixIndex + suffixMatcher.isMatch(template, suffixIndex, 0, template.length());
        }

        StringBuilder result = new StringBuilder(template.length());
        for (int i = 0; i < template.length(); ) {
            if (replacements.containsKey(i)) {
                Replacement replacement = replacements.get(i);
                result.append(replacement.value);
                i += replacement.length;
            } else {
                if (i < template.length() - 1 && template.charAt(i) == escapeChar && prefixMatcher.isMatch(template, i + 1, 0, template.length()) > 0) {
                    result.append(template, i + 1, i + 1 + prefixMatcher.isMatch(template, i + 1, 0, template.length()));
                    i += 1 + prefixMatcher.isMatch(template, i + 1, 0, template.length());
                } else {
                    result.append(template.charAt(i));
                    i++;
                }
            }
        }

        return result.toString();
    }

    private int findSuffixIndex(String buffer, int start, StringMatcher suffixMatcher) {
        for (int i = start; i < buffer.length(); ) {
            int match = suffixMatcher.isMatch(buffer, i, 0, buffer.length());
            if (match > 0) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private static class Replacement {
        final String value;
        final int length;

        Replacement(String value, int length) {
            this.value = value;
            this.length = length;
        }
    }
}
