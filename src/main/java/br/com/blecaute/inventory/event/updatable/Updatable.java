package br.com.blecaute.inventory.event.updatable;

import br.com.blecaute.inventory.callback.ItemCallback;
import br.com.blecaute.inventory.callback.ObjectCallback;
import br.com.blecaute.inventory.callback.PaginatedItemCallback;
import br.com.blecaute.inventory.callback.PaginatedObjectCallback;
import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * The class that implements the Updatable indicates that it
 * has methods to update the item in the inventory.
 */
public interface Updatable<T extends InventoryItem> {

    /**
     * Update all items and objects in inventory.
     */
    void update();

    /**
     * Update an item in the given slot.
     *
     * @param slot The slot of ItemStack to update.
     * @param item The ItemStack to set.
     */
    void updateItem(int slot, @Nullable ItemStack item);

    /**
     * Update an item in the given slot.
     *
     * @param slot The slot of ItemStack to update.
     * @param item The ItemStack to set.
     * @param callback The ItemCallback.
     */
    void updateItem(int slot, @Nullable ItemStack item, @Nullable ItemCallback<T> callback);

    /**
     * Update all items in paginated inventory with the given identifier.
     *
     * @param identifier The identifier of page.
     * @param items The collection of ItemStack to update.
     */
    void updateItems(@NotNull String identifier, @NotNull Collection<ItemStack> items);

    /**
     * Update all items in paginated inventory with the given identifier.
     *
     * @param identifier The identifier of page.
     * @param items The collection of ItemStack to update.
     * @param callback The PaginatedItemCallback.
     */
    void updateItems(@NotNull String identifier, @NotNull Collection<ItemStack> items, @Nullable PaginatedItemCallback<T> callback);

    /**
     * Update an object in the given slot.
     *
     * @param slot The slot of Object to update.
     * @param object The Object to set.
     */
    void updateObject(int slot, @NotNull T object);

    /**
     * Update an object in the given slot.
     *
     * @param slot The slot of Object to update.
     * @param object The Object to set.
     * @param callback The ObjectCallback.
     */
    void updateObject(int slot, @NotNull T object, @Nullable ObjectCallback<T> callback);

    /**
     * Update all objects in paginated inventory with the given identifier.
     *
     * @param identifier The identifier of page.
     * @param objects The collection of Object to update.
     */
    void updateObjects(@NotNull String identifier, @NotNull Collection<? extends T> objects);

    /**
     * Update all objects in paginated inventory with the given identifier.
     *
     * @param identifier The identifier of page.
     * @param objects The collection of Object to update.
     * @param callback The PaginatedObjectCallback.
     */
    void updateObjects(@NotNull String identifier, @NotNull Collection<? extends T> objects, @Nullable PaginatedObjectCallback<T> callback);

}