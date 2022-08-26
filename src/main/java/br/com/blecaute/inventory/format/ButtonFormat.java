package br.com.blecaute.inventory.format;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.button.Button;
import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * Represents inventory format for @{@link Button}.
 * @param <T> The inventory item type.
 */
public interface ButtonFormat<T extends InventoryItem> extends SimpleFormat<T>{

    /**
     * Return @{@link Button} instance for this format.
     *
     * @return The button instance.
     */
    @NotNull
    Button getButton();

    /**
     * Format inventory with given button.
     *
     * @param inventory The inventory.
     * @param builder The builder.
     */
    @Override
    default void format(@NotNull Inventory inventory, @NotNull InventoryBuilder<T> builder) {
        if (!getButton().canFormat(inventory, builder, this)) return;

        int slot = getSlot();
        if (slot >= 0 && slot < inventory.getSize()) {
            inventory.setItem(slot, getItemStack(inventory, builder));
        }
    }
}