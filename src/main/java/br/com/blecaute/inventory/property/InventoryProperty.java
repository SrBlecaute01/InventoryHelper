package br.com.blecaute.inventory.property;

import br.com.blecaute.inventory.InventoryBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

<<<<<<< HEAD
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
=======
import java.util.Optional;
>>>>>>> 4247e1ea5c243dc7d205debc171f9413231e0caa
import java.util.function.Supplier;

/**
 * The class to save properties of @{@link InventoryBuilder}
 */
public interface InventoryProperty {

    /**
     * Get the property
     *
     * @param key The key
     *
     * @return The property
     */
    @Nullable
    <T> T get(@NotNull String key);

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
    @Nullable
    <T> T getOrNull(@NotNull String key, @NotNull Class<T> clazz);

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
    @NotNull
    <T, X extends Throwable> T getOrThrow(@NotNull String key, @NotNull Class<T> clazz, @NotNull Supplier<? extends X> exception) throws X;

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
    <T> Optional<T> get(@NotNull String key, @NotNull Class<T> clazz);

    /**
     * Set the property
     *
     * @param key The key
     * @param value The value
     */
    void set(@NotNull String key, @NotNull Object value);

    /**
     * Remove property.
     *
     * @param key The key.
     *
     * @return The property removed.
     */
    @Nullable
    Object removeOrNull(@NotNull String key);

    /**
     * Remove property.
     *
     * @param key The key.
     *
     * @return The optional property removed.
     */
    Optional<Object> remove(@NotNull String key);

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
    @Nullable
    <T> T removeOrNull(@NotNull String key, @NotNull Class<T> clazz);

    /**
     * Remove property.
     *
     * @param key The key.
     * @param clazz The object class.
     *
     * @return The optional property removed.
<<<<<<< HEAD
=======
     *
>>>>>>> 4247e1ea5c243dc7d205debc171f9413231e0caa
     * @param <T> The property type.
     */
    <T> Optional<T> remove(@NotNull String key, @NotNull Class<T> clazz);

    /**
<<<<<<< HEAD
     * Get inventory property keys.
     *
     * @return The inventory property keys.
     */
    Set<String> getKeys();

    /**
     * Get inventory property values.
     *
     * @return The inventory property values.
     */
    Collection<Object> getValues();

    /**
     * Clone inventory property.
     *
     * @return The inventory property clone.
     */
    InventoryProperty deepClone();
=======
     * Clone properties.
     *
     * @return The properties clone.
     */
    InventoryProperty deepClone();

>>>>>>> 4247e1ea5c243dc7d205debc171f9413231e0caa
}