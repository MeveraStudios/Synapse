package studio.mevera.synapse.platform;

import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import studio.mevera.synapse.context.Context;
import studio.mevera.synapse.placeholder.Placeholder;

public abstract class AdventureNeuronBase<U extends User> extends NeuronBase<U> {

    private TagResolver resolver;

    public AdventureNeuronBase(final Namespace namespace) {
        super(namespace);
    }

    public TagResolver getOrFormAdventureTag() {
        if (this.resolver == null) {
            this.refreshAdventureTags();
        }
        return this.resolver;
    }

    @Override
    public void register(final Placeholder<U> placeholder) {
        super.register(placeholder);
        this.refreshAdventureTags();
    }

    @SuppressWarnings("ALL")
    public void refreshAdventureTags() {
        final TagResolver.Builder builder = TagResolver.builder();
        for (final Placeholder<U> placeholder : this.placeholders.values()) {
            builder.tag(placeholder.name(), (argumentQueue, context) -> {
                return Tag.preProcessParsed(placeholder.resolve(toContext(placeholder.name(), argumentQueue, context)));
            });
        }
        this.resolver = builder.build();
    }

    protected abstract Context<U> toContext(
            final String tag,
            net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue queue,
            net.kyori.adventure.text.minimessage.Context context
    );

}
