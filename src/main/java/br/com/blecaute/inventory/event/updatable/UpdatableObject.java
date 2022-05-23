package br.com.blecaute.inventory.event.updatable;

import br.com.blecaute.inventory.callback.ObjectCallback;
import br.com.blecaute.inventory.type.InventoryItem;
import org.jetbrains.annotations.NotNull;

/**
 * The class that implements the UpdatableObject indicates that
 * it has methods to update objects saved in the inventory.
 *
 * @param <T> The type of InventoryItem.
 */
public interface UpdatableObject<T extends InventoryItem> extends Updatable<T> {

    /**
     * Update clicked object
     *
     * @param object The object to set.
     */
    void updateObject(@NotNull T object);

    /**
     * Update clicked object.
     *
     * @param object The object to set.
     * @param callback The ObjectCallback.
     */
    void updateObject(@NotNull T object, @NotNull ObjectCallback<T> callback);

}
