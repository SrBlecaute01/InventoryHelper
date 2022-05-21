package br.com.blecaute.inventory.event.updatable;

import br.com.blecaute.inventory.callback.ObjectCallback;
import br.com.blecaute.inventory.type.InventoryItem;
import org.jetbrains.annotations.NotNull;

/**
 * The class that implements the UpdatableObject indicates that
 * it has methods to update objects saved in the inventory.
 *
 * @param <T> The type of @{@link InventoryItem}
 */
public interface UpdatableObject<T extends InventoryItem> extends Updatable<T> {

    /**
     * Update clicked object
     *
     * @param object The object
     */
    void updateObject(@NotNull T object);

    /**
     * Update object and your callback
     *
     * @param object The object
     * @param callback The @{@link ObjectCallback}
     */
    void updateObject(@NotNull T object, @NotNull ObjectCallback<T> callback);

}
