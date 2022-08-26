package br.com.blecaute.inventory.event;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.callback.ObjectCallback;
import br.com.blecaute.inventory.event.updatable.UpdatableObject;
import br.com.blecaute.inventory.format.InventoryFormat;
import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * The event called when the player clicks on an object.
 *
 * @param <T> The type of @{@link InventoryItem}
 */
public class ObjectClickEvent<T extends InventoryItem> extends ItemClickEvent<T> implements UpdatableObject<T> {

    private final T object;

    public ObjectClickEvent(@NotNull InventoryFormat<T> format,
                            @NotNull InventoryBuilder<T> builder,
                            @NotNull InventoryClickEvent event,
                            @Nullable T object) {

        super(format, builder, event);
        this.object = object;
    }

    @NotNull
    public T getObject() {
        return Objects.requireNonNull(object);
    }

    @Override
    public void updateObject(@NotNull T object) {
        updateObject(getSlot(), object);
    }

    @Override
    public void updateObject(@NotNull T object, @NotNull ObjectCallback<T> callback) {
        updateObject(getSlot(), object, callback);
    }

}
