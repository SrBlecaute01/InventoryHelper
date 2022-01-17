package br.com.blecaute.inventory.property;

import br.com.blecaute.inventory.exception.InventoryBuilderException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class InventoryProperty implements Cloneable {

    private Map<String, Object> map = new HashMap<>();

    @Nullable
    @SuppressWarnings("unchecked cast")
    public <T> T get(String key) {
        Object object = this.map.get(key);
        return object == null ? null : (T) object;
    }

    public void set(@NotNull String key, @NotNull Object value) {
        this.map.put(key, value);
    }

    @Override
    public InventoryProperty clone() {
        try {
            InventoryProperty property = (InventoryProperty) super.clone();
            property.map = new HashMap<>(this.map);

            return property;

        } catch (CloneNotSupportedException exception) {
            throw new InventoryBuilderException(exception.getMessage());
        }
    }
}
