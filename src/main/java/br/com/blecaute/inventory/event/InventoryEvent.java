package br.com.blecaute.inventory.event;

import br.com.blecaute.inventory.InventoryUpdater;
import br.com.blecaute.inventory.property.InventoryProperty;
import br.com.blecaute.inventory.type.InventoryItem;
import br.com.blecaute.inventory.InventoryBuilder;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.Validate;
import org.bukkit.entity.Player;
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
    @NotNull private final InventoryClickEvent event;

    @Getter(AccessLevel.PROTECTED)
    protected final InventoryUpdater<T> updater;

    /**
     * Create a new InventoryEvent with the given InventoryBuilder and InventoryClickEvent
     *
     * @param builder The @{@link InventoryBuilder}
     * @param event The @{@link InventoryClickEvent}
     */
    public InventoryEvent(@NotNull InventoryBuilder<T> builder, @NotNull InventoryClickEvent event) {
        Validate.notNull(builder, "builder cannot be null");
        Validate.notNull(event, "event cannot be null");

        this.builder = builder;
        this.event = event;
        this.updater = InventoryUpdater.of(builder);
    }

    /**
     * Get index of clicked slot
     *
     * @return index of clicked slot
     */
    public int getSlot() {
        return event.getRawSlot();
    }

    /**
     * Get player who clicked
     *
     * @return The @{@link Player}
     */
    public @NotNull Player getPlayer() {
        return (Player) event.getWhoClicked();
    }

    /**
     * Get clicked @{@link Inventory}
     *
     * @return The @{@link Inventory}
     */
    public @NotNull Inventory getInventory() {
        return event.getInventory();
    }

    /**
     * Get clicked @{@link ItemStack}
     *
     * @return The @{@link ItemStack}
     */
    public @NotNull ItemStack getItemStack() {
        return event.getCurrentItem();
    }

    /**
     * Get the properties of @{@link Inventory}
     *
     * @return The @{@link InventoryProperty}
     */
    public @NotNull InventoryProperty getProperties() {
        return builder.getProperties();
    }

}