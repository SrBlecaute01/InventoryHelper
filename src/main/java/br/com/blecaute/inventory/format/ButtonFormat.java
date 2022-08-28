package br.com.blecaute.inventory.format;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.button.Button;
import br.com.blecaute.inventory.type.InventoryItem;
import org.apache.commons.lang.Validate;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * Represents inventory format for a @{@link Button}.
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

    @Override
    default void accept(@NotNull InventoryClickEvent event, @NotNull InventoryBuilder<T> builder) {
        accept(event, builder, this);
    }

    default void accept(@NotNull InventoryClickEvent event,
                        @NotNull InventoryBuilder<T> builder,
                        @NotNull InventoryFormat<T> format) {

        Validate.notNull(event, "event cannot be null");
        Validate.notNull(builder, "builder cannot be null");

        getButton().accept(event, builder, format);
    }

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

    /**
     * Update button in inventory.
     *
     * @param inventory The inventory.
     * @param builder The builder.
     * @param caller The formatter that was updated.
     *
     * @return True if button was updated, false otherwise.
     */
    default boolean update(@NotNull Inventory inventory, @NotNull InventoryBuilder<T> builder, @NotNull InventoryFormat<T> caller) {
        int slot = getSlot();
        if (slot >= 0) {
            boolean format = getButton().canFormat(inventory, builder, caller);
            inventory.setItem(slot, format ? getItemStack(inventory, builder) : null);
            return true;
        }

        return false;
    }

}