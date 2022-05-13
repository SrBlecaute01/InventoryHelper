package br.com.blecaute.inventory.format.impl;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.callback.ItemCallback;
import br.com.blecaute.inventory.callback.ObjectCallback;
import br.com.blecaute.inventory.event.ObjectClickEvent;
import br.com.blecaute.inventory.format.SimpleFormat;
import br.com.blecaute.inventory.format.updater.ObjectUpdater;
import br.com.blecaute.inventory.type.InventoryItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Data @AllArgsConstructor
public class SimpleObjectFormat<T extends InventoryItem> implements SimpleFormat<T>, ObjectUpdater<T> {

    private int slot;

    @Setter @Nullable
    private ItemStack icon;

    @Nullable private T object;
    @Nullable private ObjectCallback<T> callBack;

    public SimpleObjectFormat(int slot, @Nullable T object, @Nullable ObjectCallback<T> callBack) {
        this.slot = slot;
        this.object = object;
        this.callBack = callBack;
    }

    @Override
    public void accept(@NotNull InventoryClickEvent event, @NotNull InventoryBuilder<T> builder) {
        if (this.callBack != null) {
            this.callBack.accept(new ObjectClickEvent<>(this, builder, event, object));
        }
    }

    @Override
    public ItemStack getItemStack(@NotNull Inventory inventory, @NotNull InventoryBuilder<T> builder) {
        if (this.icon != null) {
            return this.icon;
        }

        return this.object == null ? null : this.object.getItem(inventory, builder.getProperties());
    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory) {
        format(inventory, builder);
    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory, int slot,
                       @Nullable ItemStack itemStack, @Nullable ItemCallback<T> callback) {

        this.callBack = callback == null ? this.callBack : callback::accept;
        this.icon = itemStack;
        this.slot = slot;

        format(inventory, builder);
    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory, int slot,
                       @NotNull T object, @Nullable ObjectCallback<T> callback) {

        this.slot = slot;
        this.object = object;
        this.callBack = callback == null ? this.callBack : callback;

        format(inventory, builder);
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
