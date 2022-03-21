package br.com.blecaute.inventory.format.impl;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.callback.ObjectCallback;
import br.com.blecaute.inventory.event.ObjectClickEvent;
import br.com.blecaute.inventory.format.InventoryFormat;
import br.com.blecaute.inventory.format.UpdatableFormat;
import br.com.blecaute.inventory.type.InventoryItem;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Getter
public class SimpleObjectFormat<T extends InventoryItem> implements InventoryFormat<T>, UpdatableFormat<ObjectCallback<T>> {

    private int slot;

    @NonNull private T object;

    @Nullable private ObjectCallback<T> callBack;
    @Nullable private ItemStack itemStack;

    private boolean flush = false;

    public SimpleObjectFormat(int slot, @NonNull T object, @Nullable ObjectCallback<T> callBack) {
        this.slot = slot;
        this.object = object;
        this.callBack = callBack;
    }

    @Override
    public boolean isValid(int slot) {
        return this.slot == slot;
    }

    @Override
    public void accept(@NotNull InventoryClickEvent event, @NotNull InventoryBuilder<T> builder) {
        if (this.callBack != null) {
            this.callBack.accept(new ObjectClickEvent<>(this, builder, event, object));
        }
    }

    @Override
    public void format(@NotNull Inventory inventory, @NotNull InventoryBuilder<T> builder) {
        inventory.setItem(slot, this.itemStack == null ? object.getItem(inventory, builder.getProperties()) : this.itemStack);
    }

    @Override
    public void update(int slot, @Nullable ItemStack itemStack, @Nullable ObjectCallback<T> callback) {
        this.slot = slot;
        this.itemStack = itemStack;
        this.callBack = callback == null ? this.callBack : callback;
        this.flush = false;
    }

    @Override
    public void flush(@NotNull Inventory inventory) {
        if (flush && slot >= 0) {
            inventory.setItem(slot, itemStack);
            flush = false;
        }
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
