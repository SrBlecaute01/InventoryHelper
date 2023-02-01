package br.com.blecaute.inventory.property;

import br.com.blecaute.inventory.exception.InventoryBuilderException;
import br.com.blecaute.inventory.InventoryBuilder;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    @Nullable
    @SuppressWarnings("unchecked cast")
    public <T> T get(@NotNull String key) {
        Preconditions.checkNotNull(key, "key cannot be null");

        Object object = this.map.get(key);
        return object == null ? null : (T) object;
    }

    /**
     * Get the property
     *
     * @param key The key.
     * @param clazz The object class.
     *
     * @return The object.
     * @param <T> The object type.
     */
    @Nullable
    public <T> T getOrNull(@NotNull String key, @NotNull Class<T> clazz) {
        Preconditions.checkNotNull(key, "key cannot be null");
        Preconditions.checkNotNull(clazz, "class cannot be null");

        Object object = this.map.get(key);
        return object == null ? null : clazz.cast(object);
    }

    /**
     * Get the property.
     *
     * @param key The key.
     * @param clazz The object class.
     *
     * @return The optional object.
     * @param <T> The object type.
     */
    public <T> Optional<T> get(@NotNull String key, @NotNull Class<T> clazz) {
        return Optional.ofNullable(getOrNull(key, clazz));
    }

    /**
     * Set the property
     *
     * @param key The key
     * @param value The value
     */
    public void set(@NotNull String key, @NotNull Object value) {
        Preconditions.checkNotNull(key, "key cannot be null");
        Preconditions.checkNotNull(value, "value cannot be null");

        this.map.put(key, value);
    }

    /**
     * Remove property.
     *
     * @param key The key.
     *
     * @return The object removed.
     */
    @Nullable
    public Object removeOrNull(@NotNull String key) {
        Preconditions.checkNotNull(key, "key cannot be null");
        return this.map.remove(key);
    }

    /**
     * Remove property.
     *
     * @param key The key.
     *
     * @return The optional object removed.
     */
    public Optional<Object> remove(@NotNull String key) {
        return Optional.ofNullable(removeOrNull(key));
    }

    /**
     * Remove property.
     *
     * @param key The key.
     * @param clazz The object class.
     *
     * @return The object removed.
     * @param <T> The object type.
     */
    @Nullable
    public <T> T removeOrNull(@NotNull String key, @NotNull Class<T> clazz) {
        Preconditions.checkNotNull(key, "key cannot be null");
        Preconditions.checkNotNull(clazz, "class cannot be null");

        return clazz.cast(this.map.remove(key));
    }

    /**
     * Remove property.
     *
     * @param key The key.
     * @param clazz The object class.
     *
     * @return The optional object removed.
     * @param <T> The object type.
     */
    public <T> Optional<T> remove(@NotNull String key, @NotNull Class<T> clazz) {
        return Optional.ofNullable(removeOrNull(key, clazz));
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
