package br.com.blecaute.inventory.format;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * The class that implements the Simple Format indicates that
 * it is able to update the inventory with items.
 *
 * @param <T> The type of @{@link InventoryItem}
 */
public interface SimpleFormat<T extends InventoryItem> extends InventoryFormat<T> {

    /**
     * Get the slot of item
     *
     * @return The slot
     */
    int getSlot();

    /**
     * Get the @{@link ItemStack}
     *
     * @param inventory The @{@link Inventory}
     * @param builder The @{@link InventoryBuilder}
     *
     * @return The @{@link ItemStack}
     */
    ItemStack getItemStack(@NotNull Inventory inventory, @NotNull InventoryBuilder<T> builder);

    /**
     * Check if clicked slot is valid to handle click event.
     *
     * @param slot The clicked slot
     *
     * @return True if valid, false otherwise
     */
    @Override
    default boolean isValid(int slot) {
        return getSlot() == slot;
    }

    /**
     * Format @{@link Inventory}
     *
     * @param inventory The @{@link Inventory}
     * @param builder The @{@link InventoryBuilder}
     */
    default void format(@NotNull Inventory inventory, @NotNull InventoryBuilder<T> builder) {
        inventory.setItem(getSlot(), getItemStack(inventory, builder));
    }

}
