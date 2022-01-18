package br.com.blecaute.inventory.format;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.callback.ItemCallback;
import br.com.blecaute.inventory.event.InventoryEvent;
import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represent interface to format @{@link Inventory}
 */
public interface InventoryFormat<T extends InventoryItem> {

    /**
     * Check if clicked slot is valid
     *
     * @param slot  The slot
     *
     * @return true if slot is valid.
     */
    boolean isValid(int slot);

    /**
     * Accept click
     *
     * @param event     The @{@link InventoryClickEvent}
     * @param builder   The @{@link InventoryBuilder}
     */
    void accept(@NotNull InventoryClickEvent event, @NotNull InventoryBuilder<T> builder);

    /**
     * Call the callback of item.
     *
     * @param callback  The @{@link ItemCallback}
     * @param event     The @{@link InventoryEvent}
     */
    default void accept(@Nullable ItemCallback<T> callback, @NotNull InventoryEvent<T> event) {
        if (callback != null) {
            callback.accept(event);
        }
    }

    /**
     * Format inventory
     *
     * @param inventory The @{@link Inventory}
     * @param builder   The @{@link InventoryBuilder}
     */
    void format(@NotNull Inventory inventory, @NotNull InventoryBuilder<T> builder);

}