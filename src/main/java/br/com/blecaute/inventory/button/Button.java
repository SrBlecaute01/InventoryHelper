package br.com.blecaute.inventory.button;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.button.impl.NextPageButtonImpl;
import br.com.blecaute.inventory.button.impl.PreviousPageButtonImpl;
import br.com.blecaute.inventory.format.InventoryFormat;
import br.com.blecaute.inventory.type.InventoryItem;
import br.com.blecaute.inventory.type.InventorySlot;
import org.apache.commons.lang.Validate;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The Button represents an item in the inventory with an action already configured.
 */
public interface Button extends InventorySlot {

    /**
     * Accept the inventory event and execute an action.
     *
     * @param event The event.
     * @param builder The builder.
     * @param format The format.
     * @param <T> The inventory item type.
     */
    <T extends InventoryItem> void accept(@NotNull InventoryEvent event, @NotNull InventoryBuilder<T> builder,
                                          @NotNull InventoryFormat<T> format);

    /**
     * Check if button can be placed in inventory.
     *
     * @param inventory The inventory.
     * @param builder The builder.
     * @param format The format.
     *
     * @return True if button can be placed, otherwise false.
     *
     * @param <T> The inventory item type.
     */
    <T extends InventoryItem> boolean canFormat(@NotNull Inventory inventory, @NotNull InventoryBuilder<T> builder,
                                                @NotNull InventoryFormat<T> format);

    /**
     * Create a new button with given type, slot and item.
     *
     * @param type The type.
     * @param slot The slot.
     * @param itemStack The item.
     *
     * @return The button instance.
     */
    @NotNull
    @Contract("_, _, _ -> new")
    static Button of(@NotNull ButtonType type, int slot, ItemStack itemStack) {
        return of(type,  slot, itemStack, false);
    }

    /**
     * Create a new button with given type, slot, item and always show option.
     *
     * @param type The type.
     * @param slot The slot.
     * @param itemStack The item.
     * @param alwaysShow The option to always show item.
     *
     * @return The button instance.
     */
    @NotNull
    @Contract("_, _, _, _ -> new")
    static Button of(@NotNull ButtonType type, int slot, @NotNull ItemStack itemStack, boolean alwaysShow) {
        Validate.notNull(type, "button type cannot be null");
        Validate.notNull(itemStack, "itemStack cannot be null");

        if (type == ButtonType.NEXT_PAGE) {
            return new NextPageButtonImpl(slot, itemStack, alwaysShow);
        }

        return new PreviousPageButtonImpl(slot, itemStack, alwaysShow);
    }

}