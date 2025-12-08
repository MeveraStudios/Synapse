package studio.mevera.synapse.platform;

import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import studio.mevera.synapse.context.Context;
import studio.mevera.synapse.placeholder.Placeholder;
import studio.mevera.synapse.type.ComponentHandler;

public abstract class AdventureNeuronBase<U extends User> extends NeuronBase<U> {

    private TagResolver resolver;

    public AdventureNeuronBase(final Namespace namespace) {
        super(namespace);
    }

    @Override
    protected void initializeDefaultTypeHandlers() {
        super.initializeDefaultTypeHandlers();
        this.registerTypeHandler(new ComponentHandler());
    }

    public TagResolver getOrFormAdventureTag() {
        if (this.resolver == null) {
            this.refreshAdventureTags();
        }
        return this.resolver;
    }

    @Override
    public void register(final Placeholder<U> placeholder, final String... aliases) {
        super.register(placeholder, aliases);
        this.refreshAdventureTags();
    }

    @Override
    public void unregister(final String tag) {
        super.unregister(tag);
        this.refreshAdventureTags();
    }

    @SuppressWarnings("ALL")
    public void refreshAdventureTags() {
        final TagResolver.Builder builder = TagResolver.builder();
        for (final Placeholder<U> placeholder : this.placeholders.values()) {
            if (placeholder.isRelational()) continue;
            for (final String namespace : this.namespace.getNames()) {
                builder.tag((namespace.isEmpty() ? "" : (namespace + "_")) + placeholder.name(), (argumentQueue, context) -> {
                    final Object resolved = placeholder.resolve(toContext(placeholder.name(), argumentQueue, context));
                    if (resolved instanceof Tag tag) {
                        return tag;
                    }
                    if (resolved instanceof ComponentLike component) {
                        return Tag.inserting(component);
                    }
                    return Tag.preProcessParsed(this.handleType(resolved));
                });
            }
        }
        this.resolver = builder.build();
    }

    protected abstract Context<U> toContext(
            final String tag,
            net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue queue,
            net.kyori.adventure.text.minimessage.Context context
    );

}
