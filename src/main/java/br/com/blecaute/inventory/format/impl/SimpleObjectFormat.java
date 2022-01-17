package br.com.blecaute.inventory.format.impl;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.InventoryClick;
import br.com.blecaute.inventory.format.InventoryFormat;
import br.com.blecaute.inventory.property.InventoryProperty;
import br.com.blecaute.inventory.type.InventoryItemType;
import lombok.Data;
import lombok.NonNull;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@Data
public class SimpleObjectFormat<T extends InventoryItemType> implements InventoryFormat<T> {

    private final int slot;
    @NonNull private final T object;
    private final Consumer<InventoryClick<T>> consumer;

    @Override
    public boolean isValid(int slot) {
        return this.slot == slot;
    }

    @Override
    public void accept(@NotNull InventoryClickEvent event, @NotNull InventoryBuilder<T> builder) {
        accept(consumer, new InventoryClick<>(event, event.getCurrentItem(), object));
    }

    @Override
    public void format(@NotNull Inventory inventory, @NotNull InventoryProperty property) {
        inventory.setItem(slot, object.getItem(inventory, property));
    }

}
