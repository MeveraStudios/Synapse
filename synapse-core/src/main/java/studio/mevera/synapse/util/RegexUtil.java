package studio.mevera.synapse.util;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class RegexUtil {

    private RegexUtil() {
        // Utility class, prevent instantiation
    }

    private final static int INITIAL_CAPACITY = 5; // Initial capacity guess for the list of arguments
    private static final String[] EMPTY_ARGS = new String[0];
    public static final Pattern PLACEHOLDER_PATTERN = Pattern.compile(
            "\\$\\{([\\w.-]+)((?::\"[^\"]*\"|:'[^']*'|:`[^`]*`|:[^:\\s{}]*?)*)}"
    );

    public static String[] extractArguments(final String rawArgs) {
        if (rawArgs == null || rawArgs.isEmpty()) {
            return EMPTY_ARGS;
        }

        final List<String> args = new ArrayList<>(INITIAL_CAPACITY);

        final int length = rawArgs.length();

        for (int i = 0; i < length; i++) {
            if (rawArgs.charAt(i) != ':') {
                continue; // Skip non-delimiter chars
            }

            if (++i >= length) break; // Skip ':' and check bounds

            final char delimiter = rawArgs.charAt(i);

            if (delimiter == '"' || delimiter == '\'' || delimiter == '`') {
                final int start = ++i; // Skip opening quote
                final int end = rawArgs.indexOf(delimiter, i);
                if (end == -1) break;
                args.add(rawArgs.substring(start, end));
                i = end; // Will be incremented by loop
            } else {
                final int start = i;
                while (i < length && rawArgs.charAt(i) != ':') {
                    i++;
                }
                args.add(rawArgs.substring(start, i));
                i--; // Compensate for loop increment
            }
        }

        return args.toArray(String[]::new);
    }

}
