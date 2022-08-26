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
import org.jetbrains.annotations.Nullable;

public interface Button extends InventorySlot {

    <T extends InventoryItem> void accept(@NotNull InventoryEvent event, @NotNull InventoryBuilder<T> builder, @NotNull InventoryFormat<T> format);

    <T extends InventoryItem> boolean canFormat(@NotNull Inventory inventory, @NotNull InventoryBuilder<T> builder, @NotNull InventoryFormat<T> format);

    @NotNull
    @Contract("_, _, _ -> new")
    static Button of(@NotNull ButtonType type, ItemStack itemStack, int slot) {
        return of(type, itemStack, slot, false);
    }

    @NotNull
    @Contract("_, _, _, _ -> new")
    static Button of(@NotNull ButtonType type, @NotNull ItemStack itemStack, int slot, boolean alwaysShow) {
        Validate.notNull(type, "button type cannot be null");
        Validate.notNull(itemStack, "itemStack cannot be null");

        if (type == ButtonType.NEXT_PAGE) {
            return new NextPageButtonImpl(slot, itemStack, alwaysShow);
        }

        return new PreviousPageButtonImpl(slot, itemStack, alwaysShow);
    }

}