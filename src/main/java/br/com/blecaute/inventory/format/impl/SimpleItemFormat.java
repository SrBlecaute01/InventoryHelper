package br.com.blecaute.inventory.format.impl;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.callback.ItemCallback;
import br.com.blecaute.inventory.event.ItemClickEvent;
import br.com.blecaute.inventory.format.InventoryFormat;
import br.com.blecaute.inventory.format.UpdatableFormat;
import br.com.blecaute.inventory.type.InventoryItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter @AllArgsConstructor
public class SimpleItemFormat<T extends InventoryItem> implements InventoryFormat<T>, UpdatableFormat<ItemCallback<T>> {

    private int slot;

    @Nullable private ItemStack itemStack;
    @Nullable private ItemCallback<T> callBack;

    private boolean flush = false;

    public SimpleItemFormat(int slot, @Nullable ItemStack itemStack, @Nullable ItemCallback<T> callBack) {
        this.slot = slot;
        this.itemStack = itemStack;
        this.callBack = callBack;
    }

    @Override
    public boolean isValid(int slot) {
        return this.slot == slot;
    }

    @Override
    public void accept(@NotNull InventoryClickEvent event, @NotNull InventoryBuilder<T> builder) {
        if (this.itemStack != null && this.callBack != null) {
            this.callBack.accept(new ItemClickEvent<>(this, builder, event));
        }
    }

    @Override
    public void format(@NotNull Inventory inventory, @NotNull InventoryBuilder<T> builder) {
        inventory.setItem(slot, itemStack);
    }

    @Override
    public void update(int slot, @Nullable ItemStack itemStack, @Nullable ItemCallback<T> callback) {
        this.slot = slot;
        this.itemStack = itemStack;
        this.callBack = callback == null ? this.callBack : callback;
        this.flush = true;
    }

    @Override
    public void flush(@NotNull Inventory inventory) {
        if (flush && slot >= 0) {
            inventory.setItem(slot, itemStack);
            flush = false;
        }
    }

}
