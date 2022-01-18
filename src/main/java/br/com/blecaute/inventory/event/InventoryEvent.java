package br.com.blecaute.inventory.event;

import br.com.blecaute.inventory.property.InventoryProperty;
import br.com.blecaute.inventory.type.InventoryItem;
import br.com.blecaute.inventory.InventoryBuilder;
import lombok.Data;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
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
     */
    @Nullable
    private final T object;
}