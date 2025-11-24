package net.sapfii.modutilities.config;

import java.io.Serializable;
import java.util.List;

public class ConfigOption<T extends Serializable> {
    private ConfigOption() {}
    private T value;
    public T get() { return value; }
    public void set(T value) { this.value = value; }

    public static <M extends Serializable> ConfigOption<M> build(M initialValue) {
        ConfigOption<M> configOption = new ConfigOption<>();
        configOption.set(initialValue);
        return configOption;
    }

    @SuppressWarnings("unchecked")
    public void cycle() {
        if (value instanceof Boolean bool) value = (T) Boolean.valueOf(!bool);
        if (!value.getClass().isEnum()) return;
        T[] constants = (T[]) value.getClass().getEnumConstants();
        int index = List.of(constants).indexOf(value);
        index = (index + 1) % constants.length;
        value = constants[index];
    }

    public boolean is(T value) {
        return this.value == value;
    }
}
