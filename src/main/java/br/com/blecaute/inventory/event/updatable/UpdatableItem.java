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
 * @param <T> The type of @{@link InventoryItem}
 */
public interface UpdatableItem<T extends InventoryItem> extends Updatable {

    /**
     * Update clicked @{@link ItemStack}
     *
     * @param itemStack the @{@link ItemStack}
     */
    void update(@NotNull ItemStack itemStack);

    /**
     * Update clicked @{@link ItemStack} and change your callback
     *
     * @param itemStack The @{@link ItemStack}
     * @param callback The @{@link ItemCallback}
     */
    void update(@NotNull ItemStack itemStack, @Nullable ItemCallback<T> callback);

    /**
     * Update @{@link ItemStack} in inventory
     *
     * @param slot The slot of inventory
     * @param itemStack The @{@link ItemStack}
     * @param callback The @{@link ItemCallback}
     */
    void update(int slot, @NotNull ItemStack itemStack, @Nullable ItemCallback<T> callback);
}