package br.com.blecaute.inventory.event;

import br.com.blecaute.inventory.InventoryUpdater;
import br.com.blecaute.inventory.property.InventoryProperty;
import br.com.blecaute.inventory.type.InventoryItem;
import br.com.blecaute.inventory.InventoryBuilder;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represent the click event of @{@link InventoryBuilder}.
 */
@Data
public abstract class InventoryEvent<T extends InventoryItem> {

    @NotNull private final InventoryBuilder<T> builder;
    @NotNull private final InventoryClickEvent event;

    @Getter(AccessLevel.PROTECTED)
    protected final InventoryUpdater<T> updater;

    /**
     * Create a new InventoryEvent with the given InventoryBuilder and InventoryClickEvent.
     *
     * @param builder The @{@link InventoryBuilder}.
     * @param event The @{@link InventoryClickEvent}.
     */
    public InventoryEvent(@NotNull InventoryBuilder<T> builder, @NotNull InventoryClickEvent event) {
        Validate.notNull(builder, "builder cannot be null");
        Validate.notNull(event, "event cannot be null");

        this.builder = builder;
        this.event = event;
        this.updater = InventoryUpdater.of(builder);
    }

    /**
     * Get index of clicked slot.
     *
     * @return index of clicked slot.
     */
    public int getSlot() {
        return event.getRawSlot();
    }

    /**
     * Get player who clicked.
     *
     * @return The @{@link Player}.
     */
    @NotNull
    public Player getPlayer() {
        return (Player) event.getWhoClicked();
    }

    /**
     * Send message to who clicked.
     *
     * @param message The message.
     */
    public void sendMessage(@NotNull String message) {
        Preconditions.checkNotNull(message, "message cannot be null");
        event.getWhoClicked().sendMessage(message);
    }

    /**
     * Get clicked @{@link Inventory}.
     *
     * @return The @{@link Inventory}.
     */
    @NotNull
    public Inventory getInventory() {
        return event.getInventory();
    }

    /**
     * Get clicked @{@link ItemStack}.
     *
     * @return The @{@link ItemStack}.
     */
    @NotNull
    public ItemStack getItemStack() {
        return this.event.getCurrentItem();
    }

    /**
     * Get the properties of @{@link Inventory}.
     *
     * @return The @{@link InventoryProperty}.
     */
    @NotNull
    public InventoryProperty getProperties() {
        return this.builder.getProperties();
    }

    /**
     * Get property of @{@link Inventory}.
     *
     * @param key The key of property.
     * @param <S> The type of property.
     *
     * @return The property.
     */
    @Nullable
    @Deprecated
    public <S> S getProperty(@NotNull String key) {
        return builder.getProperties().get(key);
    }

    /**
     * Set property in @{@link Inventory}.
     *
     * @param key The key of property.
     * @param value The value of property.
     */
    @Deprecated
    public void setProperty(@NotNull String key, @NotNull Object value) {
        builder.getProperties().set(key, value);
    }

}