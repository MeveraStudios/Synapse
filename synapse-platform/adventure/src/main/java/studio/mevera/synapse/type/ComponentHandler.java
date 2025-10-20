package studio.mevera.synapse.type;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class ComponentHandler extends BaseTypeHandler<Component> {

    @Override
    public String handle(Component input) {
        return MiniMessage.miniMessage().serialize(input);
    }

}
