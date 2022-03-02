package br.com.blecaute.inventory.event;

import br.com.blecaute.inventory.property.InventoryProperty;
import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The event called when the player clicks on an object.
 *
 * @param <T> The type of @{@link InventoryItem}
 */
public class ObjectClickEvent<T extends InventoryItem> extends ItemClickEvent<T> {

    private final T object;

    public ObjectClickEvent(@NotNull InventoryClickEvent event, @NotNull ItemStack itemStack,
                            @NotNull InventoryProperty properties, @NotNull T object) {

        super(event, itemStack, properties);
        this.object = object;
    }

    /**
     * Get the object
     *
     * @return The object of @{@link InventoryItem}
     */
    @Override
    @SuppressWarnings("deprecation")
    public @Nullable T getObject() {
        return object;
    }
}
