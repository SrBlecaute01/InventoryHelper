package br.com.blecaute.inventory.property;

import br.com.blecaute.inventory.exception.InventoryBuilderException;
import br.com.blecaute.inventory.InventoryBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * The class to save properties of @{@link InventoryBuilder}
 */
public class InventoryProperty implements Cloneable {

    private Map<String, Object> map = new HashMap<>();

    /**
     * Get the property
     *
     * @param key The key
     *
     * @return The property
     */
    @Nullable @SuppressWarnings("unchecked cast")
    public <T> T get(String key) {
        Object object = this.map.get(key);
        return object == null ? null : (T) object;
    }

    /**
     * Set the property
     *
     * @param key The key
     * @param value The value
     */
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
