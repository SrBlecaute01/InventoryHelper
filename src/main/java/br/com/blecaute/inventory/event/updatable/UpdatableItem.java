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
public interface UpdatableItem<T extends InventoryItem> extends Updatable<T> {

    /**
     * Update clicked @{@link ItemStack}
     *
     * @param itemStack the @{@link ItemStack}
     */
    void updateItem(@Nullable ItemStack itemStack);

    /**
     * Update clicked @{@link ItemStack} and change your callback
     *
     * @param itemStack The @{@link ItemStack}
     * @param callback The @{@link ItemCallback}
     */
    void updateItem(@Nullable ItemStack itemStack, @NotNull ItemCallback<T> callback);

}