package br.com.blecaute.inventory.event.updatable;

import br.com.blecaute.inventory.callback.PaginatedItemCallback;
import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * The class that implements the UpdatableItemPaginated indicates that
 * it has methods to update items in paginated inventories.
 *
 * @param <T> The type of InventoryItem.
 */
public interface UpdatableItemPaginated<T extends InventoryItem> extends UpdatableItem<T> {

    /**
     * Update all items of paginated inventory.
     *
     * @param items The collection of ItemStack to update.
     */
    void updateItems(@NotNull Collection<ItemStack> items);

    /**
     * Update all items of paginated inventory.
     *
     * @param items The collection of ItemStack to update.
     * @param callback The PaginatedItemCallback.
     */
    void updateItems(@NotNull Collection<ItemStack> items, @NotNull PaginatedItemCallback<T> callback);

}