package studio.mevera.synapse.impl;

import studio.mevera.synapse.platform.NeuronBase;
import studio.mevera.synapse.platform.Namespace;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class TestNeuron extends NeuronBase<TestUser> {

    private final TestSynapse synapse;

    public TestNeuron(final TestSynapse synapse) {
        super(Namespace.of("test"));

        this.synapse = synapse;
        // static placeholders
        AtomicInteger i = new AtomicInteger();
        this.register("meows", () -> "refreshes: " + i.getAndIncrement(), options -> options.async(true).delay(500, TimeUnit.MILLISECONDS).refresh(true));

        // contextual placeholders
        this.register("hello", context -> "Hello there, " + context.user().origin().name() + "!");
        this.register("optional", context -> Optional.of("Hello there, " + context.user().origin().name() + "!"));
        this.register("welcome", context -> "Welcome " + context.user().origin().name() + "! You have " + context.arguments()[0] + " new messages.");

        // type tests
        this.register("number", context -> 42);
        this.register("boolean", context -> true);
        this.register("collection", context -> java.util.List.of("one", "two", "three"));

        this.register("cached", context -> UUID.randomUUID().toString(), options -> {
            options.cache(true);
            options.cacheTTL(50, TimeUnit.SECONDS);
        });

        this.register("arguments", context -> {
            final String[] args = context.arguments();
            return "Arguments(" + args.length + "): " + String.join(", ", args);
        });

        this.registerRelational("compare", context -> context.user().name() + "-" + context.other().name());
    }

    @Override
    public void register() {
        this.synapse.registerNeuron(this);
    }

}
