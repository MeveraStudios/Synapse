package studio.mevera.synapse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import studio.mevera.synapse.util.RegexUtil;

import java.util.regex.Matcher;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RegexUtil")
class RegexUtilTest {

    // =========================================================================
    // extractArguments — null / empty
    // =========================================================================

    @Nested
    @DisplayName("extractArguments() — null and empty input")
    class NullAndEmpty {

        @ParameterizedTest(name = "input = [{0}]")
        @NullAndEmptySource
        void returnsEmptyArray(String input) {
            assertArrayEquals(new String[0], RegexUtil.extractArguments(input));
        }

        @Test
        @DisplayName("returns the same EMPTY_ARGS sentinel (no allocation)")
        void returnsSameEmptyInstance() {
            assertSame(RegexUtil.extractArguments(null),
                    RegexUtil.extractArguments(""));
        }
    }

    // =========================================================================
    // extractArguments — no delimiters
    // =========================================================================

    @Nested
    @DisplayName("extractArguments() — no colon delimiters")
    class NoDelimiters {

        @ParameterizedTest(name = "input = [{0}]")
        @ValueSource(strings = {"foo", "foo.bar", "some_key", "   "})
        void returnsEmptyArray(String input)  {
            assertArrayEquals(new String[0], RegexUtil.extractArguments(input));
        }
    }

    // =========================================================================
    // extractArguments — unquoted arguments
    // =========================================================================

    @Nested
    @DisplayName("extractArguments() — unquoted arguments")
    class Unquoted {

        @Test
        @DisplayName("single unquoted arg")
        void singleUnquoted() {
            assertArrayEquals(new String[]{"default"},
                    RegexUtil.extractArguments(":default"));
        }

        @Test
        @DisplayName("two unquoted args")
        void twoUnquoted() {
            assertArrayEquals(new String[]{"foo", "bar"},
                    RegexUtil.extractArguments(":foo:bar"));
        }

        @Test
        @DisplayName("three unquoted args")
        void threeUnquoted() {
            assertArrayEquals(new String[]{"a", "b", "c"},
                    RegexUtil.extractArguments(":a:b:c"));
        }

        @Test
        @DisplayName("unquoted arg with dots and underscores")
        void unquotedWithSpecialChars() {
            assertArrayEquals(new String[]{"some.default_value"},
                    RegexUtil.extractArguments(":some.default_value"));
        }

        @Test
        @DisplayName("trailing colon does not produce empty arg")
        void trailingColon() {
            // ":foo:" — the segment after the last ':' is empty, should be ignored
            String[] result = RegexUtil.extractArguments(":foo:");
            assertArrayEquals(new String[]{"foo"}, result);
        }

        @Test
        @DisplayName("leading colon only — produces no args")
        void colonOnly() {
            assertArrayEquals(new String[0], RegexUtil.extractArguments(":"));
        }

        @Test
        @DisplayName("consecutive colons produce empty string arguments")
        void consecutiveColons() {
            String[] result = RegexUtil.extractArguments("::");
            assertEquals(1, result.length);
            assertEquals("", result[0]);
        }
    }

    // =========================================================================
    // extractArguments — double-quoted arguments
    // =========================================================================

    @Nested
    @DisplayName("extractArguments() — double-quoted arguments")
    class DoubleQuoted {

        @Test
        @DisplayName("single double-quoted arg")
        void singleDoubleQuoted() {
            assertArrayEquals(new String[]{"hello world"},
                    RegexUtil.extractArguments(":\"hello world\""));
        }

        @Test
        @DisplayName("empty double-quoted arg")
        void emptyDoubleQuoted() {
            assertArrayEquals(new String[]{""},
                    RegexUtil.extractArguments(":\"\""));
        }

        @Test
        @DisplayName("double-quoted arg with colons inside")
        void doubleQuotedWithColons() {
            assertArrayEquals(new String[]{"a:b:c"},
                    RegexUtil.extractArguments(":\"a:b:c\""));
        }

        @Test
        @DisplayName("double-quoted arg with single quotes inside")
        void doubleQuotedWithSingleQuotes() {
            assertArrayEquals(new String[]{"it's fine"},
                    RegexUtil.extractArguments(":\"it's fine\""));
        }

        @Test
        @DisplayName("double-quoted arg followed by unquoted arg")
        void doubleQuotedThenUnquoted() {
            assertArrayEquals(new String[]{"hello world", "plain"},
                    RegexUtil.extractArguments(":\"hello world\":plain"));
        }

        @Test
        @DisplayName("multiple double-quoted args")
        void multipleDoubleQuoted() {
            assertArrayEquals(new String[]{"first arg", "second arg"},
                    RegexUtil.extractArguments(":\"first arg\":\"second arg\""));
        }
    }

    // =========================================================================
    // extractArguments — single-quoted arguments
    // =========================================================================

    @Nested
    @DisplayName("extractArguments() — single-quoted arguments")
    class SingleQuoted {

        @Test
        @DisplayName("single single-quoted arg")
        void singleSingleQuoted() {
            assertArrayEquals(new String[]{"hello world"},
                    RegexUtil.extractArguments(":'hello world'"));
        }

        @Test
        @DisplayName("empty single-quoted arg")
        void emptySingleQuoted() {
            assertArrayEquals(new String[]{""},
                    RegexUtil.extractArguments(":''"));
        }

        @Test
        @DisplayName("single-quoted arg with colons inside")
        void singleQuotedWithColons() {
            assertArrayEquals(new String[]{"x:y"},
                    RegexUtil.extractArguments(":'x:y'"));
        }

        @Test
        @DisplayName("single-quoted arg with double quotes inside")
        void singleQuotedWithDoubleQuotes() {
            assertArrayEquals(new String[]{"say \"hi\""},
                    RegexUtil.extractArguments(":'say \"hi\"'"));
        }

        @Test
        @DisplayName("multiple single-quoted args")
        void multipleSingleQuoted() {
            assertArrayEquals(new String[]{"foo bar", "baz qux"},
                    RegexUtil.extractArguments(":'foo bar':'baz qux'"));
        }
    }

    // =========================================================================
    // extractArguments — backtick-quoted arguments
    // =========================================================================

    @Nested
    @DisplayName("extractArguments() — backtick-quoted arguments")
    class BacktickQuoted {

        @Test
        @DisplayName("single backtick-quoted arg")
        void singleBacktick() {
            assertArrayEquals(new String[]{"hello world"},
                    RegexUtil.extractArguments(":`hello world`"));
        }

        @Test
        @DisplayName("empty backtick-quoted arg")
        void emptyBacktick() {
            assertArrayEquals(new String[]{""},
                    RegexUtil.extractArguments(":``"));
        }

        @Test
        @DisplayName("backtick-quoted arg with colons and quotes inside")
        void backtickWithMixedChars() {
            assertArrayEquals(new String[]{"a:b\"c'd"},
                    RegexUtil.extractArguments(":`a:b\"c'd`"));
        }

        @Test
        @DisplayName("multiple backtick-quoted args")
        void multipleBacktick() {
            assertArrayEquals(new String[]{"one two", "three four"},
                    RegexUtil.extractArguments(":`one two`:`three four`"));
        }
    }

    // =========================================================================
    // extractArguments — mixed quote styles
    // =========================================================================

    @Nested
    @DisplayName("extractArguments() — mixed quote styles")
    class MixedQuotes {

        @Test
        @DisplayName("double-quoted then single-quoted")
        void doubleQuotedThenSingleQuoted() {
            assertArrayEquals(new String[]{"hello world", "foo bar"},
                    RegexUtil.extractArguments(":\"hello world\":'foo bar'"));
        }

        @Test
        @DisplayName("unquoted then double-quoted then backtick")
        void unquotedDoubleBacktick() {
            assertArrayEquals(new String[]{"plain", "with spaces", "also spaces"},
                    RegexUtil.extractArguments(":plain:\"with spaces\":`also spaces`"));
        }

        @Test
        @DisplayName("backtick then unquoted then single-quoted")
        void backtickUnquotedSingle() {
            assertArrayEquals(new String[]{"bt", "raw", "sq"},
                    RegexUtil.extractArguments(":`bt`:raw:'sq'"));
        }

        @Test
        @DisplayName("all three quote types in sequence")
        void allThreeQuoteTypes() {
            assertArrayEquals(new String[]{"dq", "sq", "bt"},
                    RegexUtil.extractArguments(":\"dq\":'sq':`bt`"));
        }
    }

    // =========================================================================
    // extractArguments — malformed / edge cases
    // =========================================================================

    @Nested
    @DisplayName("extractArguments() — malformed and edge cases")
    class Malformed {

        @Test
        @DisplayName("unclosed double-quote returns partial results up to that point")
        void unclosedDoubleQuote() {
            // ":good:\"unclosed  — "good" should be captured, then parsing aborts
            String[] result = RegexUtil.extractArguments(":good:\"unclosed");
            assertArrayEquals(new String[]{"good"}, result);
        }

        @Test
        @DisplayName("unclosed single-quote returns partial results")
        void unclosedSingleQuote() {
            String[] result = RegexUtil.extractArguments(":good:'unclosed");
            assertArrayEquals(new String[]{"good"}, result);
        }

        @Test
        @DisplayName("unclosed backtick returns partial results")
        void unclosedBacktick() {
            String[] result = RegexUtil.extractArguments(":good:`unclosed");
            assertArrayEquals(new String[]{"good"}, result);
        }

        @Test
        @DisplayName("colon at very end of string does not throw")
        void colonAtEnd() {
            assertDoesNotThrow(() -> RegexUtil.extractArguments(":"));
        }

        @Test
        @DisplayName("rawArgs containing only whitespace returns empty array")
        void whitespaceOnly() {
            // no colon → no args
            assertArrayEquals(new String[0], RegexUtil.extractArguments("   "));
        }

        @Test
        @DisplayName("unquoted arg followed immediately by end of string")
        void unquotedAtEnd() {
            assertArrayEquals(new String[]{"value"},
                    RegexUtil.extractArguments(":value"));
        }
    }

    // =========================================================================
    // PLACEHOLDER_PATTERN — basic matching
    // =========================================================================

    @Nested
    @DisplayName("PLACEHOLDER_PATTERN — matching")
    class PlaceholderPatternMatch {

        private Matcher matcher(String input) {
            return RegexUtil.PLACEHOLDER_PATTERN.matcher(input);
        }

        @Test
        @DisplayName("simple placeholder with no args")
        void simplePlaceholder() {
            Matcher m = matcher("${foo}");
            assertTrue(m.find());
            assertEquals("foo", m.group(1));
            assertEquals("", m.group(2));
        }

        @Test
        @DisplayName("placeholder with dotted key")
        void dottedKey() {
            Matcher m = matcher("${foo.bar.baz}");
            assertTrue(m.find());
            assertEquals("foo.bar.baz", m.group(1));
        }

        @Test
        @DisplayName("placeholder with hyphenated key")
        void hyphenatedKey() {
            Matcher m = matcher("${my-key}");
            // '-' is literal in [\w.-], so it DOES match
            assertTrue(m.find());
            assertEquals("my-key", m.group(1));
        }

        @Test
        @DisplayName("placeholder with underscore key")
        void underscoreKey() {
            Matcher m = matcher("${my_key}");
            assertTrue(m.find());
            assertEquals("my_key", m.group(1));
        }

        @Test
        @DisplayName("placeholder with unquoted arg")
        void withUnquotedArg() {
            Matcher m = matcher("${foo:default}");
            assertTrue(m.find());
            assertEquals("foo", m.group(1));
            assertEquals(":default", m.group(2));
        }

        @Test
        @DisplayName("placeholder with double-quoted arg")
        void withDoubleQuotedArg() {
            Matcher m = matcher("${foo:\"hello world\"}");
            assertTrue(m.find());
            assertEquals("foo", m.group(1));
            assertEquals(":\"hello world\"", m.group(2));
        }

        @Test
        @DisplayName("placeholder with single-quoted arg")
        void withSingleQuotedArg() {
            Matcher m = matcher("${foo:'hello world'}");
            assertTrue(m.find());
            assertEquals("foo", m.group(1));
            assertEquals(":'hello world'", m.group(2));
        }

        @Test
        @DisplayName("placeholder with backtick-quoted arg")
        void withBacktickArg() {
            Matcher m = matcher("${foo:`hello world`}");
            assertTrue(m.find());
            assertEquals("foo", m.group(1));
            assertEquals(":`hello world`", m.group(2));
        }

        @Test
        @DisplayName("placeholder with multiple args")
        void withMultipleArgs() {
            Matcher m = matcher("${foo:bar:baz}");
            assertTrue(m.find());
            assertEquals("foo", m.group(1));
            assertEquals(":bar:baz", m.group(2));
        }

        @Test
        @DisplayName("does not match plain text")
        void noMatch() {
            assertFalse(matcher("no placeholders here").find());
        }

        @Test
        @DisplayName("does not match ${} with empty key")
        void emptyKey() {
            assertFalse(matcher("${}").find());
        }

        @Test
        @DisplayName("finds multiple placeholders in a string")
        void multiplePlaceholders() {
            Matcher m = matcher("Hello ${name}, you are ${age} years old.");
            assertTrue(m.find());
            assertEquals("name", m.group(1));
            assertTrue(m.find());
            assertEquals("age", m.group(1));
            assertFalse(m.find());
        }

        @Test
        @DisplayName("placeholder embedded in larger text")
        void embeddedPlaceholder() {
            Matcher m = matcher("prefix_${key.sub:default}_suffix");
            assertTrue(m.find());
            assertEquals("key.sub", m.group(1));
            assertEquals(":default", m.group(2));
        }
    }

    // =========================================================================
    // Integration — pattern group(2) fed into extractArguments
    // =========================================================================

    @Nested
    @DisplayName("Integration — PLACEHOLDER_PATTERN + extractArguments()")
    class Integration {

        private String[] extractFromPlaceholder(String input) {
            Matcher m = RegexUtil.PLACEHOLDER_PATTERN.matcher(input);
            assertTrue(m.find(), "Pattern should match: " + input);
            return RegexUtil.extractArguments(m.group(2));
        }

        @Test
        @DisplayName("no args → empty array")
        void noArgs() {
            assertArrayEquals(new String[0], extractFromPlaceholder("${key}"));
        }

        @Test
        @DisplayName("single unquoted arg")
        void singleUnquotedArg() {
            assertArrayEquals(new String[]{"fallback"},
                    extractFromPlaceholder("${key:fallback}"));
        }

        @Test
        @DisplayName("double-quoted arg with spaces")
        void doubleQuotedWithSpaces() {
            assertArrayEquals(new String[]{"hello world"},
                    extractFromPlaceholder("${key:\"hello world\"}"));
        }

        @Test
        @DisplayName("multiple unquoted args")
        void multipleUnquotedArgs() {
            assertArrayEquals(new String[]{"a", "b", "c"},
                    extractFromPlaceholder("${key:a:b:c}"));
        }

        @Test
        @DisplayName("mixed quoted and unquoted args")
        void mixedArgs() {
            assertArrayEquals(new String[]{"plain", "with spaces"},
                    extractFromPlaceholder("${key:plain:\"with spaces\"}"));
        }

        @Test
        @DisplayName("backtick arg round-trip")
        void backtickRoundTrip() {
            assertArrayEquals(new String[]{"bt value"},
                    extractFromPlaceholder("${key:`bt value`}"));
        }
    }
}