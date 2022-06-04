package br.com.blecaute.inventory.format.impl;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.callback.ItemCallback;
import br.com.blecaute.inventory.event.ItemClickEvent;
import br.com.blecaute.inventory.format.SimpleFormat;
import br.com.blecaute.inventory.format.updater.ItemUpdater;
import br.com.blecaute.inventory.type.InventoryItem;
import lombok.Data;
import org.apache.commons.lang.Validate;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Data
public class SimpleItemFormat<T extends InventoryItem> implements SimpleFormat<T>, ItemUpdater<T> {

    private int slot;
    private ItemStack itemStack;
    private ItemCallback<T> callBack;

    public SimpleItemFormat(int slot, @Nullable ItemStack itemStack, @Nullable ItemCallback<T> callBack) {
        this.slot = slot;
        this.itemStack = itemStack;
        this.callBack = callBack;
    }

    @Override
    public void accept(@NotNull InventoryClickEvent event, @NotNull InventoryBuilder<T> builder) {
        if (this.itemStack != null && this.callBack != null) {
            this.callBack.accept(new ItemClickEvent<>(this, builder, event));
        }
    }

    @Override
    public ItemStack getItemStack(@NotNull Inventory inventory, @NotNull InventoryBuilder<T> builder) {
        return this.itemStack;
    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory,
                       @Nullable ItemStack itemStack, int slot) {

        Validate.notNull(builder, "builder cannot be null");
        Validate.notNull(inventory, "inventory cannot be null");

        this.itemStack = itemStack;

        format(inventory, builder);
    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory,
                       @Nullable ItemCallback<T> callback, @Nullable ItemStack itemStack,
                       int slot) {

        update(builder, inventory, itemStack, slot);

        this.callBack = callback;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof SimpleObjectFormat) {
            SimpleObjectFormat<?> that = (SimpleObjectFormat<?>) o;
            return getSlot() == that.getSlot();
        }

        if (o instanceof SimpleItemFormat) {
            SimpleItemFormat<?> that = (SimpleItemFormat<?>) o;
            return getSlot() == that.getSlot();
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSlot());
    }
}
