package br.com.blecaute.inventory.event;

import br.com.blecaute.inventory.property.InventoryProperty;
import br.com.blecaute.inventory.type.InventoryItem;
import br.com.blecaute.inventory.InventoryBuilder;
import lombok.Data;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represent the click event of @{@link InventoryBuilder}
 */
@Data
public class InventoryEvent<T extends InventoryItem> {

    /**
     * The click event
     */
    @NotNull private final InventoryClickEvent event;

    /**
     * The clicked item
     */
    @NotNull private final ItemStack itemStack;

    /**
     * The properties of @{@link InventoryBuilder}
     */
    @NotNull private final InventoryProperty properties;

    /**
     * The object
     *
     * @deprecated Please use this parameter
     * only in @{@link ObjectClickEvent}
     *
     */
    private final T object;

    /**
     * The object of @{@link InventoryItem}
     *
     * @deprecated Please use this method
     * only in @{@link ObjectClickEvent}
     *
     */
    @Nullable
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.3.2")
    public T getObject() {
        return object;
    }
}