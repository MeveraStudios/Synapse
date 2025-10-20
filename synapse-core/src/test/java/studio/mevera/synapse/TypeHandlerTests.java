package studio.mevera.synapse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import studio.mevera.synapse.type.TypeHandlerRegistry;
import studio.mevera.synapse.type.impl.*;
import studio.mevera.synapse.type.impl.date.DurationHandler;
import studio.mevera.synapse.type.impl.date.InstantHandler;
import studio.mevera.synapse.type.impl.date.LocalDateTimeHandler;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class TypeHandlerTests {

    private TypeHandlerRegistry createRegistry() {
        TypeHandlerRegistry registry = new TypeHandlerRegistry();

        // primitive / basic handlers
        registry.register(new EnumHandler());
        registry.register(new StringHandler());
        registry.register(new NumberHandler());
        registry.register(new BooleanHandler());

        // date/time handlers
        registry.register(new DurationHandler());
        registry.register(new InstantHandler());
        registry.register(new LocalDateTimeHandler());

        // collection / map / array handlers
        registry.register(new MapHandler(registry));
        registry.register(new ArrayHandler(registry));
        registry.register(new CollectionHandler(registry));

        // wrappers
        registry.register(new SupplierHandler(registry));
        registry.register(new OptionalHandler(registry));
        registry.register(new CompletableFutureHandler(registry));

        return registry;
    }

    private enum SampleEnum {
        FIRST_VALUE,
        SECOND_THING
    }

    @Test
    public void testEnumAndPrimitives() {
        TypeHandlerRegistry registry = createRegistry();

        // enum -> Title Case
        Assertions.assertEquals("First Value", registry.handle(SampleEnum.FIRST_VALUE));
        Assertions.assertEquals("Second Thing", registry.handle(SampleEnum.SECOND_THING));

        // string
        Assertions.assertEquals("hello", registry.handle("hello"));

        // number
        Assertions.assertEquals("42", registry.handle(42));
        Assertions.assertEquals("3.14", registry.handle(3.14));

        // boolean
        Assertions.assertEquals("true", registry.handle(true));
        Assertions.assertEquals("false", registry.handle(false));
    }

    @Test
    public void testDateHandlers() {
        TypeHandlerRegistry registry = createRegistry();

        // Duration
        Assertions.assertEquals("1h 1m 1s", registry.handle(Duration.ofSeconds(3661)));
        Assertions.assertEquals("1m 1s", registry.handle(Duration.ofSeconds(61)));
        Assertions.assertEquals("59s", registry.handle(Duration.ofSeconds(59)));

        // Instant
        Instant instant = Instant.parse("2020-01-01T00:00:00Z");
        Assertions.assertEquals("2020-01-01T00:00:00Z", registry.handle(instant));

        // LocalDateTime
        LocalDateTime ldt = LocalDateTime.of(2020, 1, 1, 12, 34, 56);
        Assertions.assertEquals("2020-01-01 12:34:56", registry.handle(ldt));
    }

    @Test
    public void testCollectionsMapsAndArrays() {
        TypeHandlerRegistry registry = createRegistry();

        // verify array handler was registered
        Assertions.assertNotNull(registry.getHandler(Object[].class), "Array handler should be registered for Object[].class");

        // collection
        List<String> list = List.of("one", "two", "three");
        Assertions.assertEquals("one, two, three", registry.handle(list));

        // empty collection -> empty string
        Assertions.assertEquals("", registry.handle(new ArrayList<>()));

        // array
        String[] array = new String[]{"a", "b"};
        Assertions.assertEquals("a, b", registry.handle(array));

        // empty array
        Assertions.assertEquals("", registry.handle(new String[0]));

        // map (use LinkedHashMap to preserve order)
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        Assertions.assertEquals("a=1, b=2", registry.handle(map));

        // empty map -> empty string
        Assertions.assertEquals("", registry.handle(new HashMap<>()));
    }

    @Test
    public void testWrappersAndAsync() {
        TypeHandlerRegistry registry = createRegistry();

        // supplier
        Supplier<String> supplier = () -> "supplied";
        Assertions.assertEquals("supplied", registry.handle(supplier));

        // optional
        Optional<String> opt = Optional.of("present");
        Assertions.assertEquals("present", registry.handle(opt));

        // empty optional -> underlying registry handles null -> null
        Optional<String> empty = Optional.empty();
        Assertions.assertNull(registry.handle(empty));

        // completable future - completed
        CompletableFuture<Integer> done = CompletableFuture.completedFuture(7);
        Assertions.assertEquals("7", registry.handle(done));

        // completable future - pending -> null
        CompletableFuture<String> pending = new CompletableFuture<>();
        Assertions.assertNull(registry.handle(pending));

        // completable future - cancelled -> null
        CompletableFuture<String> cancelled = new CompletableFuture<>();
        cancelled.cancel(true);
        Assertions.assertNull(registry.handle(cancelled));

        // completable future - completed exceptionally -> null
        CompletableFuture<String> exceptional = new CompletableFuture<>();
        exceptional.completeExceptionally(new RuntimeException("fail"));
        Assertions.assertNull(registry.handle(exceptional));
    }

}
