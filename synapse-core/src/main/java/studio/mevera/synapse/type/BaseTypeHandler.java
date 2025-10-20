package studio.mevera.synapse.type;

import java.lang.reflect.Type;

public abstract class BaseTypeHandler<T> extends TypeCapturer implements TypeHandler<T> {

    private final Type type;

    protected BaseTypeHandler() {
        this.type = this.extractType(BaseTypeHandler.class, 0);
    }

    protected BaseTypeHandler(Type type) {
        this.type = type;
    }

    @Override
    public Type getType() {
        return type;
    }

}
