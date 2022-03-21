package br.com.blecaute.inventory.event;

import br.com.blecaute.inventory.property.InventoryProperty;
import br.com.blecaute.inventory.type.InventoryItem;
import br.com.blecaute.inventory.InventoryBuilder;
import lombok.Data;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

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

    public Inventory getInventory() {
        return event.getInventory();
    }

    public ItemStack getItemStack() {
        return event.getCurrentItem();
    }

    public InventoryProperty getProperties() {
        return builder.getProperties();
    }

}