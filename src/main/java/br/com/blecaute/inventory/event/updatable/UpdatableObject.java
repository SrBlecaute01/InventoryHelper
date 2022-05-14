package br.com.blecaute.inventory.event.updatable;

import br.com.blecaute.inventory.callback.ObjectCallback;
import br.com.blecaute.inventory.type.InventoryItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The class that implements the UpdatableObject indicates that
 * it has methods to update objects saved in the inventory.
 *
 * @param <T> The type of @{@link InventoryItem}
 */
public interface UpdatableObject<T extends InventoryItem> extends Updatable {

    /**
     * Update all objects of inventory
     */
    void update();

    /**
     * Update clicked object
     *
     * @param object The object
     */
    void update(@NotNull T object);

    /**
     * Update object and your callback
     *
     * @param object The object
     * @param callback The @{@link ObjectCallback}
     */
    void update(@NotNull T object, @Nullable ObjectCallback<T> callback);

    /**
     * Update object in inventory
     *
     * @param slot The slot
     * @param object The object
     * @param callback The @{@link ObjectCallback}
     */
    void update(int slot, @NotNull T object, @Nullable ObjectCallback<T> callback);

}
