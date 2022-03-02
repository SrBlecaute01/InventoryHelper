package br.com.blecaute.inventory.format;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * Interface to format @{@link Inventory} in @{@link InventoryBuilder}
 *
 * @param <T> The type of @{@link InventoryItem}
 */
public interface InventoryFormat<T extends InventoryItem> {

    /**
     * Check if clicked slot is valid
     *
     * @param slot The slot
     * @return true if slot is valid.
     */
    boolean isValid(int slot);

    /**
     * Accept click event
     *
     * @param event The @{@link InventoryClickEvent}
     * @param builder The @{@link InventoryBuilder}
     */
    void accept(@NotNull InventoryClickEvent event, @NotNull InventoryBuilder<T> builder);

    /**
     * Format inventory
     *
     * @param inventory The @{@link Inventory}
     * @param builder The @{@link InventoryBuilder}
     */
    void format(@NotNull Inventory inventory, @NotNull InventoryBuilder<T> builder);

}