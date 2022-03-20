package br.com.blecaute.inventory.event;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.format.InventoryFormat;
import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

/**
 * The event called when the player clicks on an object.
 *
 * @param <T> The type of @{@link InventoryItem}
 */
public class ObjectClickEvent<T extends InventoryItem> extends ItemClickEvent<T> {

    private final T object;

    public ObjectClickEvent(@NotNull InventoryFormat<T> format,
                            @NotNull InventoryBuilder<T> builder,
                            @NotNull InventoryClickEvent event,
                            @NotNull T object) {

        super(format, builder, event);
        this.object = object;
    }

    /**
     * Get the object
     *
     * @return The object of @{@link InventoryItem}
     */
    @Override
    @SuppressWarnings("deprecation")
    @NotNull
    public T getObject() {
        return object;
    }
}
