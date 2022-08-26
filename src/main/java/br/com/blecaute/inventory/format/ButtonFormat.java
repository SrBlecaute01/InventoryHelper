package br.com.blecaute.inventory.format;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.button.Button;
import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public interface ButtonFormat<T extends InventoryItem> extends SimpleFormat<T>{

    @NotNull
    Button getButton();

    @Override
    default void format(@NotNull Inventory inventory, @NotNull InventoryBuilder<T> builder) {
        if (!getButton().canFormat(inventory, builder, this)) return;

        int slot = getSlot();
        if (slot >= 0 && slot < inventory.getSize()) {
            inventory.setItem(slot, getItemStack(inventory, builder));
        }
    }
}