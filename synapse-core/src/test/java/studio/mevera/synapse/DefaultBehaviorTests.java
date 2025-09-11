package studio.mevera.synapse;

import studio.mevera.synapse.impl.TestNeuron;
import studio.mevera.synapse.impl.TestOrigin;
import studio.mevera.synapse.impl.TestSynapse;
import studio.mevera.synapse.impl.TestUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DefaultBehaviorTests {

    private static final TestSynapse testSynapse = new TestSynapse();

    private final TestOrigin testOrigin = new TestOrigin("TestUser");
    private final TestUser testUser = testSynapse.asUser(testOrigin);

    private final TestOrigin otherOrigin = new TestOrigin("OtherUser");
    private final TestUser otherUser = testSynapse.asUser(otherOrigin);

    @BeforeAll
    public static void setup() {
        testSynapse.registerNeuron(new TestNeuron());
    }

    @Test
    public void testParsing() {
        Assertions.assertEquals(
            "Hello there, TestUser!",
            testSynapse.translate("${test.hello}", testOrigin)
        );
    }

    @Test
    public void testParsingWithArgs() {
        Assertions.assertEquals(
            "Welcome TestUser! You have 5 new messages.",
            testSynapse.translate("${test.welcome:5}", testOrigin)
        );

        Assertions.assertEquals(
                "Arguments(3): \"arg1\", arg2, arg3",
                testSynapse.translate("${test.arguments:'\"arg1\"':arg2:arg3}", testOrigin)
        );
    }

    @Test
    public void testCaching() {
        String first = testSynapse.translate("${test.cached}", testOrigin);
        String second = testUser.getCachedValue("test:cached", new String[]{});

        Assertions.assertEquals(first, second, "The placeholder value should be cached and not change");
    }

    @Test
    public void testDynamicParsing() throws InterruptedException {
        String first = testSynapse.translate("${test.meows}", testOrigin);
        Thread.sleep(800);
        String second = testSynapse.translate("${test.meows}", testOrigin);

        Assertions.assertNotEquals(first, second, "The placeholder value should have been refreshed");
    }

    @Test
    public void testRelationalPlaceholder() {
        Assertions.assertEquals(
                "TestUser-OtherUser",
                testSynapse.translate("${test.compare}", testUser, otherUser)
        );
    }

}
