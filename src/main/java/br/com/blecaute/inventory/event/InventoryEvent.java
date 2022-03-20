package br.com.blecaute.inventory.event;

import br.com.blecaute.inventory.property.InventoryProperty;
import br.com.blecaute.inventory.type.InventoryItem;
import br.com.blecaute.inventory.InventoryBuilder;
import lombok.Data;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represent the click event of @{@link InventoryBuilder}
 */
@Data
public abstract class InventoryEvent<T extends InventoryItem> {

    @NotNull private final InventoryBuilder<T> builder;

    /**
     * The click event
     */
    @NotNull private final InventoryClickEvent event;

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

    public Inventory getInventory() {
        return event.getInventory();
    }

    public ItemStack getItemStack() {
        return event.getCurrentItem();
    }

    public InventoryProperty getProperties() {
        return builder.getProperties();
    }

    public abstract void update(int slot, ItemStack itemStack);

}