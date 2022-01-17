package br.com.blecaute.inventory.format.impl;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.InventoryClick;
import br.com.blecaute.inventory.format.InventoryFormat;
import br.com.blecaute.inventory.property.InventoryProperty;
import br.com.blecaute.inventory.type.InventoryItemType;
import lombok.Data;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@Data
public class SimpleItemFormat<T extends InventoryItemType> implements InventoryFormat<T> {

    private final int slot;
    private final ItemStack itemStack;
    private final Consumer<InventoryClick<T>> consumer;

    @Override
    public boolean isValid(int slot) {
        return this.slot == slot;
    }

    @Override
    public void accept(@NotNull InventoryClickEvent event, @NotNull InventoryBuilder<T> builder) {
        accept(consumer, new InventoryClick<>(event, itemStack, null));
    }

    @Override
    public void format(@NotNull Inventory inventory, @NotNull InventoryProperty property) {
        inventory.setItem(slot, itemStack);
    }

}
