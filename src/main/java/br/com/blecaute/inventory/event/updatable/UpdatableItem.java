package br.com.blecaute.inventory.event.updatable;

import br.com.blecaute.inventory.callback.ItemCallback;
import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The class that implements the UpdatableItem indicates that
 * it has methods to update items in the inventory.
 *
 * @param <T> The type of InventoryItem.
 */
public interface UpdatableItem<T extends InventoryItem> extends Updatable<T> {

    /**
     * Update clicked ItemStack
     *
     * @param itemStack the ItemStack to set.
     */
    void updateItem(@Nullable ItemStack itemStack);

    /**
     * Update clicked ItemStack.
     *
     * @param itemStack The ItemStack to set.
     * @param callback The ItemCallback.
     */
    void updateItem(@Nullable ItemStack itemStack, @NotNull ItemCallback<T> callback);

}