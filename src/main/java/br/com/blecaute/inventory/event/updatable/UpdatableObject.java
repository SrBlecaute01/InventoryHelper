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
public interface UpdatableObject<T extends InventoryItem> extends Updatable<T> {

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
    void update(@NotNull T object, @NotNull ObjectCallback<T> callback);

}
