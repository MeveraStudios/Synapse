package studio.mevera.synapse.impl;

import studio.mevera.synapse.platform.NeuronBase;
import studio.mevera.synapse.platform.Namespace;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class TestNeuron extends NeuronBase<TestUser> {

    public TestNeuron() {
        super(Namespace.of("test"));

        // static placeholders
        AtomicInteger i = new AtomicInteger();
        this.register("meows", () -> "refreshes: " + i.getAndIncrement(), options -> options.async(true).delay(500, TimeUnit.MILLISECONDS).refresh(true));

        // contextual placeholders
        this.register("hello", context -> "Hello there, " + context.user().origin().name() + "!");
        this.register("welcome", context -> "Welcome " + context.user().origin().name() + "! You have " + context.arguments()[0] + " new messages.");

        this.register("cached", context -> UUID.randomUUID().toString(), options -> {
            options.cache(true);
            options.cacheTTL(50, TimeUnit.SECONDS);
        });

        this.register("arguments", context -> {
            final String[] args = context.arguments();
            return "Arguments(" + args.length + "): " + String.join(", ", args);
        });
    }

}
