package br.com.blecaute.inventory.format.impl;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.button.Button;
import br.com.blecaute.inventory.format.ButtonFormat;
import br.com.blecaute.inventory.type.InventoryItem;
import org.apache.commons.lang.Validate;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ButtonFormatImpl<T extends InventoryItem> implements ButtonFormat<T> {

    private final Button button;

    public ButtonFormatImpl(@NotNull Button button) {
        Validate.notNull(button, "button cannot be null");

        this.button = button;
    }

    @Override
    public @NotNull Button getButton() {
        return this.button;
    }

    @Override
    public void accept(@NotNull InventoryClickEvent event, @NotNull InventoryBuilder<T> builder) {
        this.button.accept(event, builder, this);
    }

    @Override
    public int getSlot() {
        return this.button.getSlot();
    }

    @Override
    public ItemStack getItemStack(@NotNull Inventory inventory, @NotNull InventoryBuilder<T> builder) {
        return this.button.getItem(inventory, builder.getProperties());
    }
}