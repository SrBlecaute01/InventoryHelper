package br.com.blecaute.inventory.event;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.format.InventoryFormat;
import br.com.blecaute.inventory.type.InventoryItem;
import lombok.Getter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

/**
 * The event called when the player clicks on an object.
 *
 * @param <T> The type of @{@link InventoryItem}
 */
public class ObjectClickEvent<T extends InventoryItem> extends InventoryEvent<T> {

    /**
     * The @{@link InventoryFormat}
     */
    private final InventoryFormat<T> format;

    /**
     * The object
     */
    @Getter @NotNull
    private final T object;

    public ObjectClickEvent(@NotNull InventoryFormat<T> format,
                            @NotNull InventoryBuilder<T> builder,
                            @NotNull InventoryClickEvent event,
                            @NotNull T object) {

        super(builder, event);
        this.format = format;
        this.object = object;
    }


}
