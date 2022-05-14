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
import org.apache.commons.lang.Validate;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Data @AllArgsConstructor
public class SimpleObjectFormat<T extends InventoryItem> implements SimpleFormat<T>, ObjectUpdater<T> {

    private int slot;
    private ItemStack icon;

    private T object;
    private ObjectCallback<T> callBack;

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
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory,
                       @NotNull ItemStack itemStack, int slot) {

        Validate.notNull(itemStack, "itemStack cannot be null");
        validate(builder, inventory, slot);

        if (this.slot == slot) {
            this.icon = itemStack;

            format(inventory, builder);
            return;
        }

        SimpleItemFormat<T> format = new SimpleItemFormat<>(slot, itemStack, null);
        format.format(inventory, builder);

        builder.addFormat(format);
    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory,
                       @Nullable ItemCallback<T> callback, @NotNull ItemStack itemStack, int slot) {

        Validate.notNull(itemStack, "itemStack cannot be null");
        validate(builder, inventory, slot);

        if (this.slot == slot) {
            this.icon = itemStack;
            this.callBack = callback != null ? callback::accept : null;

            format(inventory, builder);
            return;
        }

        SimpleItemFormat<T> format = new SimpleItemFormat<>(slot, itemStack, callback);
        format.format(inventory, builder);

        builder.addFormat(format);
    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory) {
        Validate.notNull(builder, "builder cannot be null");
        Validate.notNull(inventory, "inventory cannot be null");

        format(inventory, builder);
    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory,
                       @NotNull T object, int slot) {

        Validate.notNull(object, "object cannot be null");
        validate(builder, inventory, slot);

        if (this.slot == slot) {
            this.object = object;
            this.icon = null;

            format(inventory, builder);
            return;
        }

        SimpleObjectFormat<T> format = new SimpleObjectFormat<>(slot, object, null);
        format.format(inventory, builder);

        builder.addFormat(format);
    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory,
                       @Nullable ObjectCallback<T> callback, @NotNull T object, int slot) {

        Validate.notNull(object, "object cannot be null");
        validate(builder, inventory, slot);

        if (this.slot == slot) {
            this.object = object;
            this.icon = null;
            this.callBack = callback;

            format(inventory, builder);
            return;
        }

        SimpleObjectFormat<T> format = new SimpleObjectFormat<>(slot, object, callback);
        format.format(inventory, builder);

        builder.addFormat(format);
    }

    private void validate(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory, int slot) {
        Validate.notNull(builder, "builder cannot be null");
        Validate.notNull(inventory, "inventory cannot be null");
        Validate.isTrue(
                slot >= 0 && slot < inventory.getSize(),
                "slot must be between 0 and " + inventory.getSize()
        );
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
