package br.com.blecaute.inventory.event;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.callback.ObjectCallback;
import br.com.blecaute.inventory.format.InventoryFormat;
import br.com.blecaute.inventory.format.updater.ObjectUpdater;
import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * The event called when the player clicks on an object.
 * @param <T> The type of @{@link InventoryItem}
 */
public class ObjectClickEvent<T extends InventoryItem> extends ItemClickEvent<T> implements UpdatableObject<T> {

    private final InventoryFormat<T> format;
    private final T object;

    public ObjectClickEvent(@NotNull InventoryFormat<T> format,
                            @NotNull InventoryBuilder<T> builder,
                            @NotNull InventoryClickEvent event,
                            @Nullable T object) {

        super(format, builder, event);
        this.format = format;
        this.object = object;
    }

    @NotNull
    public T getObject() {
        return Objects.requireNonNull(object);
    }

    @Override
    public void update(int slot, ItemStack itemStack) {
        update(slot, itemStack, null);
    }

    @Override @SuppressWarnings("unchecked")
    public void update() {
        if (!(format instanceof ObjectUpdater)) {
            throw new UnsupportedOperationException("The format is not an instance of ObjectUpdater");
        }

        ((ObjectUpdater<T>) format).update(getBuilder(), getInventory());
    }

    @Override
    public void update(@NotNull T object) {
        update(object, null);
    }

    @Override
    public void update(@NotNull T object, @Nullable ObjectCallback<T> callback) {
        update(getEvent().getRawSlot(), object, callback);
    }

    @Override @SuppressWarnings("unchecked")
    public void update(int slot, @NotNull T object, @Nullable ObjectCallback<T> callback) {
        if (!(format instanceof ObjectUpdater)) {
            throw new UnsupportedOperationException("The format is not an instance of ObjectUpdater");
        }

        ((ObjectUpdater<T>) format).update(getBuilder(), getInventory(), slot, object, callback);
    }

}
