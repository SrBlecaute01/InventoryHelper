package br.com.blecaute.inventory.property;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class InventoryProperty {

    private final Map<String, Object> map = new HashMap<>();

    @Nullable
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        Object object = this.map.get(key);
        return object == null ? null : (T) object;
    }

    public void set(@NotNull String key, @NotNull Object value) {
        this.map.put(key, value);
    }

}
