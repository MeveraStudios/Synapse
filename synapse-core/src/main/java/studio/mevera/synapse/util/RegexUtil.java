package studio.mevera.synapse.util;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class RegexUtil {

    private RegexUtil() {
        // Utility class, prevent instantiation
    }

    private final static int INITIAL_CAPACITY = 5; // Initial capacity guess for the list of arguments
    public static final Pattern PLACEHOLDER_PATTERN = Pattern.compile(
            "\\$\\{([\\w.-]+)((?::\"[^\"]*\"|:'[^']*'|:`[^`]*`|:[^:\\s{}]*?)*)}"
    );

    public static String[] extractArguments(final String rawArgs) {
        final List<String> args = new ArrayList<>(INITIAL_CAPACITY);

        int i = 0;
        while (i < rawArgs.length()) {
            if (rawArgs.charAt(i) != ':') {
                i++;
                continue;
            }
            i++; // skip ':'
            if (i >= rawArgs.length()) break;

            char delimiter = rawArgs.charAt(i);
            int end;

            if (delimiter == '"' || delimiter == '\'' || delimiter == '`') {
                i++; // skip opening quote
                end = rawArgs.indexOf(delimiter, i);
                if (end == -1) break;
                args.add(rawArgs.substring(i, end));
                i = end + 1;
            } else {
                end = i;
                while (end < rawArgs.length()) {
                    char c = rawArgs.charAt(end);
                    if (c == ':' || c == '}' || Character.isWhitespace(c)) break;
                    end++;
                }
                args.add(rawArgs.substring(i, end));
                i = end;
            }
        }

        return args.toArray(new String[0]);
    }

}
