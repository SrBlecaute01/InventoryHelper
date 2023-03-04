package br.com.blecaute.inventory.property.type;

import br.com.blecaute.inventory.property.InventoryProperty;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class AbstractInventoryProperty implements InventoryProperty {

    protected Map<String, Object> map;

    public AbstractInventoryProperty(Map<String, Object> map) {
        this.map = map;
    }

    /**
     * Get the property
     *
     * @param key The key
     *
     * @return The property
     */
    @Override
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
     * @return The property.
     *
     * @param <T> The property type.
     */
    @Override
    @Nullable
    public <T> T getOrNull(@NotNull String key, @NotNull Class<T> clazz) {
        Preconditions.checkNotNull(key, "key cannot be null");
        Preconditions.checkNotNull(clazz, "class cannot be null");

        Object object = this.map.get(key);
        return object == null ? null : clazz.cast(object);
    }

    /**
     * Get the property or throw an exception.
     *
     * @param key The key.
     * @param clazz The object class.
     * @param exception The exception supplier.
     *
     * @return The property.
     *
     * @param <T> The property type.
     * @param <X> The exception type.
     *
     * @throws X If the property is not present.
     */
    @Override
    @NotNull
    public <T, X extends Throwable> T getOrThrow(@NotNull String key, @NotNull Class<T> clazz, @NotNull Supplier<? extends X> exception) throws X {
        Preconditions.checkNotNull(exception, "supplier cannot be null");

        T object = getOrNull(key, clazz);
        if (object == null) {
            throw exception.get();
        }

        return object;
    }

    /**
     * Get the property.
     *
     * @param key The key.
     * @param clazz The object class.
     *
     * @return The optional property.
     *
     * @param <T> The property type.
     */
    @Override
    public <T> Optional<T> get(@NotNull String key, @NotNull Class<T> clazz) {
        return Optional.ofNullable(getOrNull(key, clazz));
    }

    /**
     * Set the property
     *
     * @param key The key
     * @param value The value
     */
    @Override
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
     * @return The property removed.
     */
    @Override
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
     * @return The optional property removed.
     */
    @Override
    public Optional<Object> remove(@NotNull String key) {
        return Optional.ofNullable(removeOrNull(key));
    }

    /**
     * Remove property.
     *
     * @param key The key.
     * @param clazz The object class.
     *
     * @return The property removed.
     *
     * @param <T> The property type.
     */
    @Override
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
     * @return The optional property removed.
     *
     * @param <T> The property type.
     */
    @Override
    public <T> Optional<T> remove(@NotNull String key, @NotNull Class<T> clazz) {
        return Optional.ofNullable(removeOrNull(key, clazz));
    }

    @Override
    public abstract InventoryProperty deepClone();

}