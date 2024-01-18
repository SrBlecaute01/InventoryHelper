package br.com.blecaute.inventory.property.type;

import br.com.blecaute.inventory.property.InventoryProperty;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Abstract implementation of {@link InventoryProperty}
 */
public abstract class AbstractInventoryProperty implements InventoryProperty {

    protected Map<String, Object> map;

    public AbstractInventoryProperty(@NotNull Map<String, Object> map) {
        this.map = Preconditions.checkNotNull(map, "map cannot be null");
    }


    @Override
    @Nullable
    @SuppressWarnings("unchecked cast")
    public <T> T get(@NotNull String key) {
        Preconditions.checkNotNull(key, "key cannot be null");

        Object object = this.map.get(key);
        return object == null ? null : (T) object;
    }

    @Override
    @Nullable
    public <T> T getOrNull(@NotNull String key, @NotNull Class<T> clazz) {
        Preconditions.checkNotNull(key, "key cannot be null");
        Preconditions.checkNotNull(clazz, "class cannot be null");

        Object object = this.map.get(key);
        return object == null ? null : clazz.cast(object);
    }

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

    @Override
    public <T> Optional<T> get(@NotNull String key, @NotNull Class<T> clazz) {
        return Optional.ofNullable(getOrNull(key, clazz));
    }

    @Override
    public void set(@NotNull String key, @NotNull Object value) {
        Preconditions.checkNotNull(key, "key cannot be null");
        Preconditions.checkNotNull(value, "value cannot be null");

        this.map.put(key, value);
    }

    @Override
    @Nullable
    public Object removeOrNull(@NotNull String key) {
        Preconditions.checkNotNull(key, "key cannot be null");
        return this.map.remove(key);
    }

    @Override
    public Optional<Object> remove(@NotNull String key) {
        return Optional.ofNullable(removeOrNull(key));
    }

    @Override
    @Nullable
    public <T> T removeOrNull(@NotNull String key, @NotNull Class<T> clazz) {
        Preconditions.checkNotNull(key, "key cannot be null");
        Preconditions.checkNotNull(clazz, "class cannot be null");

        return clazz.cast(this.map.remove(key));
    }

    @Override
    public <T> Optional<T> remove(@NotNull String key, @NotNull Class<T> clazz) {
        return Optional.ofNullable(removeOrNull(key, clazz));
    }

    @Override
    public Set<String> getKeys() {
        return this.map.keySet();
    }

    @Override
    public Collection<Object> getValues() {
        return this.map.values();
    }

    @Override
    public abstract InventoryProperty deepClone();

}