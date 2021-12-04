package net.lucypoulton.pronouns.api;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;

import java.util.List;
import java.util.Locale;

/**
 * Various static methods for handling strings.
 */
public class StringUtils {
    private static final Splitter splitter = Splitter.on('/').omitEmptyStrings().trimResults();

    /**
     * Splits a string by the character "/", trimming spaces from split strings and omitting empty strings.
     */
    public static List<String> splitSet(String input) {
        // mojang uses ancient versions of guava. blame them
        //noinspection UnstableApiUsage
        return splitter.splitToList(input);
    }

    /**
     * Capitalises a string.
     */
    public static String capitalise(String input) {
        if (input.length() == 0) {
            return "";
        }
        if (input.length() == 1) {
            return input.toUpperCase(Locale.ROOT);
        }
        return input.substring(0, 1).toUpperCase(Locale.ROOT) + input.substring(1).toLowerCase(Locale.ROOT);
    }
}
