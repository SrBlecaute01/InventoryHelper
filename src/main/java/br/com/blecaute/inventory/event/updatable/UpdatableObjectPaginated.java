package br.com.blecaute.inventory.event.updatable;

import br.com.blecaute.inventory.callback.PaginatedObjectCallback;
import br.com.blecaute.inventory.type.InventoryItem;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * The class that implements the UpdatableObjectPaginated indicates that
 * it has methods to update objects in paginated inventories.
 *
 * @param <T> The type of InventoryItem.
 */
public interface UpdatableObjectPaginated<T extends InventoryItem> extends UpdatableObject<T> {

    /**
     * Update all objects in paginated inventory.
     *
     * @param objects The collection of objects to update.
     */
    void updateObjects(@NotNull Collection<? extends T> objects);

    /**
     * Update all objects in paginated inventory.
     *
     * @param objects The collection of objects to update.
     * @param callback The PaginatedObjectCallback.
     */
    void updateObjects(@NotNull Collection<? extends T> objects, @NotNull PaginatedObjectCallback<T> callback);

}
