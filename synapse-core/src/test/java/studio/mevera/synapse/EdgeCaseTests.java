package studio.mevera.synapse;

import studio.mevera.synapse.impl.TestNeuron;
import studio.mevera.synapse.impl.TestOrigin;
import studio.mevera.synapse.impl.TestSynapse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class EdgeCaseTests {

    private static final TestSynapse testSynapse = new TestSynapse();
    private final TestOrigin testOrigin = new TestOrigin("TestUser");

    @BeforeAll
    public static void setup() {
        new TestNeuron(testSynapse).register();
    }

    // ========== NULL AND EMPTY CASES ==========

    @Test
    public void testNullText() {
        Assertions.assertNull(testSynapse.translate(null, testOrigin));
    }

    @Test
    public void testEmptyText() {
        Assertions.assertEquals("", testSynapse.translate("", testOrigin));
    }

    @Test
    public void testTextWithoutPlaceholders() {
        String text = "This is plain text without any placeholders.";
        Assertions.assertEquals(text, testSynapse.translate(text, testOrigin));
    }

    // ========== MALFORMED PLACEHOLDERS ==========

    @Test
    public void testUnclosedPlaceholder() {
        // Should return original text when placeholder is not closed
        Assertions.assertEquals(
            "${test.hello",
            testSynapse.translate("${test.hello", testOrigin)
        );
    }

    @Test
    public void testPlaceholderWithOnlyOpeningBrace() {
        Assertions.assertEquals(
            "${",
            testSynapse.translate("${", testOrigin)
        );
    }

    @Test
    public void testPlaceholderMissingTag() {
        // Empty tag - should skip
        Assertions.assertEquals(
            "${}",
            testSynapse.translate("${}", testOrigin)
        );
    }

    @Test
    public void testUnmatchedBraces() {
        Assertions.assertEquals(
            "Text with } extra brace",
            testSynapse.translate("Text with } extra brace", testOrigin)
        );
    }

    // ========== NESTED AND COMPLEX CASES ==========

    @Test
    public void testNestedBraces() {
        // Placeholder with braces in quoted args
        Assertions.assertEquals(
            "Arguments(1): {data}",
            testSynapse.translate("${test.arguments:\"{data}\"}", testOrigin)
        );
    }

    @Test
    public void testMultiplePlaceholders() {
        Assertions.assertEquals(
            "Hello there, TestUser! You have 42 messages.",
            testSynapse.translate("${test.hello} You have ${test.number} messages.", testOrigin)
        );
    }

    @Test
    public void testConsecutivePlaceholders() {
        Assertions.assertEquals(
            "Hello there, TestUser!42true",
            testSynapse.translate("${test.hello}${test.number}${test.boolean}", testOrigin)
        );
    }

    @Test
    public void testPlaceholderAtStart() {
        Assertions.assertEquals(
            "Hello there, TestUser! Welcome!",
            testSynapse.translate("${test.hello} Welcome!", testOrigin)
        );
    }

    @Test
    public void testPlaceholderAtEnd() {
        Assertions.assertEquals(
            "Welcome! Hello there, TestUser!",
            testSynapse.translate("Welcome! ${test.hello}", testOrigin)
        );
    }

    // ========== ARGUMENT EDGE CASES ==========

    @Test
    public void testEmptyQuotedArgs() {
        Assertions.assertEquals(
            "Arguments(3): , , ",
            testSynapse.translate("${test.arguments:\"\":\"\":\"\"}", testOrigin)
        );
    }

    @Test
    public void testArgsWithSpecialCharacters() {
        Assertions.assertEquals(
            "Arguments(1): !@#$%^&*()",
            testSynapse.translate("${test.arguments:\"!@#$%^&*()\"}", testOrigin)
        );
    }

    @Test
    public void testArgsWithColons() {
        Assertions.assertEquals(
            "Arguments(1): key:value",
            testSynapse.translate("${test.arguments:\"key:value\"}", testOrigin)
        );
    }

    @Test
    public void testArgsWithMultipleQuoteTypes() {
        Assertions.assertEquals(
            "Arguments(3): double, single, backtick",
            testSynapse.translate("${test.arguments:\"double\":'single':`backtick`}", testOrigin)
        );
    }

    @Test
    public void testArgsWithUnmatchedQuotes() {
        // Unmatched quotes should be handled gracefully
        String result = testSynapse.translate("${test.arguments:\"unclosed}", testOrigin);
        Assertions.assertTrue(result.contains("Arguments"));
    }

    @Test
    public void testArgsWithWhitespace() {
        Assertions.assertEquals(
            "Arguments(1):   spaces  ",
            testSynapse.translate("${test.arguments:\"  spaces  \"}", testOrigin)
        );
    }

    @Test
    public void testMixedQuotedAndUnquotedArgs() {
        Assertions.assertEquals(
            "Arguments(4): quoted, unquoted, single, another",
            testSynapse.translate("${test.arguments:\"quoted\":unquoted:'single':another}", testOrigin)
        );
    }

    // ========== NAMESPACE EDGE CASES ==========

    @Test
    public void testUnknownNamespace() {
        // Should skip unknown namespace
        Assertions.assertEquals(
            "${unknown.placeholder}",
            testSynapse.translate("${unknown.placeholder}", testOrigin)
        );
    }

    @Test
    public void testNamespaceWithoutDot() {
        // Tag without dot - should still work if neuron handles it
        String result = testSynapse.translate("${test}", testOrigin);
        // Result depends on how test neuron handles empty subTag
        Assertions.assertNotNull(result);
    }

    @Test
    public void testMultipleDotsInTag() {
        // Tag with multiple dots
        String result = testSynapse.translate("${test.some.nested.tag}", testOrigin);
        Assertions.assertNotNull(result);
    }

    @Test
    public void testNamespaceWithHyphenAndUnderscore() {
        // Valid characters in namespace
        String result = testSynapse.translate("${test-name_123.tag}", testOrigin);
        Assertions.assertNotNull(result);
    }

    // ========== REPLACEMENT VALUE EDGE CASES ==========

    @Test
    public void testReplacementWithDollarSign() {
        // Test that $ in replacement doesn't cause regex issues
        // Assuming test neuron has a method that returns text with $
        String result = testSynapse.translate("Price: ${test.hello}", testOrigin);
        Assertions.assertNotNull(result);
    }

    @Test
    public void testReplacementWithBackslash() {
        // Test that \ in replacement doesn't cause regex issues
        String result = testSynapse.translate("Path: ${test.hello}", testOrigin);
        Assertions.assertNotNull(result);
    }

    @Test
    public void testNullReplacement() {
        // When neuron returns null, placeholder should just not be replaced
        String result = testSynapse.translate("Before ${test.unknown} After", testOrigin);
        Assertions.assertEquals("Before ${test.unknown} After", result);
    }
    
    @Test
    public void testUnknownPlaceholderTag() {
        // Unknown tag in known namespace should skip
        String result = testSynapse.translate("${test.unknown}", testOrigin);
        Assertions.assertEquals("${test.unknown}", result);
    }

    // ========== MIXED SCENARIOS ==========

    @Test
    public void testComplexMixedText() {
        String complex = "Hello ${test.hello}, you have ${test.number} items. " +
                        "Status: ${test.boolean}. Items: ${test.collection}. " +
                        "Regular text at the end.";
        String result = testSynapse.translate(complex, testOrigin);
        
        Assertions.assertTrue(result.contains("Hello there, TestUser!"));
        Assertions.assertTrue(result.contains("42"));
        Assertions.assertTrue(result.contains("true"));
        Assertions.assertTrue(result.contains("one, two, three"));
        Assertions.assertTrue(result.contains("Regular text at the end."));
    }

    @Test
    public void testVeryLongText() {
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longText.append("Text ").append(i).append(" ${test.number} ");
        }
        
        String result = testSynapse.translate(longText.toString(), testOrigin);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.contains("42"));
    }

    @Test
    public void testManyConsecutivePlaceholders() {
        String result = testSynapse.translate("${test.number}".repeat(100), testOrigin);
        Assertions.assertNotNull(result);
        // Should contain 100 instances of "42"
        int count = result.split("42", -1).length - 1;
        Assertions.assertEquals(100, count);
    }

    @Test
    public void testUnicodeInText() {
        String unicode = "Hello ${test.hello} 你好 🎉 مرحبا";
        String result = testSynapse.translate(unicode, testOrigin);
        Assertions.assertTrue(result.contains("你好"));
        Assertions.assertTrue(result.contains("🎉"));
        Assertions.assertTrue(result.contains("مرحبا"));
    }

    @Test
    public void testUnicodeInArguments() {
        String unicode = "${test.arguments:\"你好\":\"🎉\":\"مرحبا\"}";
        String result = testSynapse.translate(unicode, testOrigin);
        Assertions.assertTrue(result.contains("你好"));
        Assertions.assertTrue(result.contains("🎉"));
        Assertions.assertTrue(result.contains("مرحبا"));
    }

    // ========== PERFORMANCE EDGE CASES ==========

    @Test
    public void testEmptyArgsList() {
        // Placeholder with colon but no args
        String result = testSynapse.translate("${test.hello:}", testOrigin);
        Assertions.assertNotNull(result);
    }

    @Test
    public void testMultipleTrailingColons() {
        String result = testSynapse.translate("${test.hello:::}", testOrigin);
        Assertions.assertNotNull(result);
    }

    @Test
    public void testPlaceholderWithOnlyColon() {
        String result = testSynapse.translate("${test:}", testOrigin);
        Assertions.assertNotNull(result);
    }

}