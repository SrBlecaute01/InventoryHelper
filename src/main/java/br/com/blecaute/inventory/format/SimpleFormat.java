package br.com.blecaute.inventory.format;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface SimpleFormat<T extends InventoryItem> extends InventoryFormat<T> {

    int getSlot();

    ItemStack getItemStack(@NotNull Inventory inventory, @NotNull InventoryBuilder<T> builder);

    @Override
    default boolean isValid(int slot) {
        return getSlot() == slot;
    }

    default void format(@NotNull Inventory inventory, @NotNull InventoryBuilder<T> builder) {
        inventory.setItem(getSlot(), getItemStack(inventory, builder));
    }

}
