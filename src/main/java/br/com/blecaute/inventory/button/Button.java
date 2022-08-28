package br.com.blecaute.inventory.button;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.button.impl.NextPageButtonImpl;
import br.com.blecaute.inventory.button.impl.PreviousPageButtonImpl;
import br.com.blecaute.inventory.format.ButtonFormat;
import br.com.blecaute.inventory.format.InventoryFormat;
import br.com.blecaute.inventory.type.InventoryItem;
import br.com.blecaute.inventory.type.InventorySlot;
import org.apache.commons.lang.Validate;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The Button represents an @{@link ItemStack} in the @{@link Inventory}
 * with an action already configured.
 */
public interface Button extends InventorySlot {

    /**
     * Check if button can be placed in @{@link Inventory}.
     *
     * @param inventory The inventory.
     * @param builder The builder.
     * @param format The format.
     *
     * @return True if button can be placed, otherwise false.
     *
     * @param <T> The {@link InventoryItem} type.
     */
    <T extends InventoryItem> boolean canFormat(@NotNull Inventory inventory,
                                                @NotNull InventoryBuilder<T> builder,
                                                @NotNull InventoryFormat<T> format);

    /**
     * Accept the @{@link InventoryEvent} and execute an action.
     *
     * @param event The event.
     * @param builder The builder.
     * @param format The format.
     *
     * @param <T> The {@link InventoryItem} type.
     */
    <T extends InventoryItem> void accept(@NotNull InventoryEvent event,
                                          @NotNull InventoryBuilder<T> builder,
                                          @NotNull InventoryFormat<T> format);

    /**
     * Update buttons of @{@link InventoryBuilder}.
     *
     * @param inventory The inventory.
     * @param builder The builder.
     * @param caller The formatter that was updated.
     *
     * @param <T> The {@link InventoryItem} type.
     */
    static <T extends InventoryItem> void update(@NotNull Inventory inventory,
                                                 @NotNull InventoryBuilder<T> builder,
                                                 @NotNull InventoryFormat<T> caller) {

        for (InventoryFormat<T> format : builder.getFormats()) {
            if (format instanceof ButtonFormat) {
                ButtonFormat<T> button = (ButtonFormat<T>) format;
                button.update(inventory, builder, caller);
            }
        }
    }

    /**
     * Create a new button with given @{@link ButtonType}, slot and @{@link ItemStack}.
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
     * Create a new button with given @{@link ButtonType}, slot, @{@link ItemStack} and always show option.
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

    /**
     * Create a new button with given type, slot and @{@link ItemStack}.
     *
     * @param type The type.
     * @param slot The slot.
     * @param itemStack The itemStack.
     *
     * @return The button instance.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    @NotNull
    @Contract("_, _, _ -> new")
    static Button of(@NotNull br.com.blecaute.inventory.enums.ButtonType type, int slot, @NotNull ItemStack itemStack) {
        return of(type, slot, itemStack, false);
    }

    /**
     * Create a new button with given type, slot, @{@link ItemStack} and always show option.
     *
     * @param type The type.
     * @param slot The slot.
     * @param itemStack The item.
     * @param alwaysShow The option to always show item.
     *
     * @return The button instance.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    @NotNull
    @Contract("_, _, _, _ -> new")
    static Button of(@NotNull br.com.blecaute.inventory.enums.ButtonType type, int slot, @NotNull ItemStack itemStack, boolean alwaysShow) {
        return of(ButtonType.valueOf(type.name()), slot, itemStack, alwaysShow);
    }

}