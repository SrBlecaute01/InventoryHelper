package br.com.blecaute.inventory;

import br.com.blecaute.inventory.button.Button;
import br.com.blecaute.inventory.callback.*;
import br.com.blecaute.inventory.configuration.InventoryConfiguration;
import br.com.blecaute.inventory.configuration.PaginatedConfiguration;
import br.com.blecaute.inventory.exception.InventoryBuilderException;
import br.com.blecaute.inventory.format.InventoryFormat;
import br.com.blecaute.inventory.format.impl.*;
import br.com.blecaute.inventory.handler.UpdateHandler;
import br.com.blecaute.inventory.property.InventoryProperty;
import br.com.blecaute.inventory.type.InventoryItem;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * A simple class for building of @{@link Inventory}.
 * @param <T> The type of @{@link InventoryBuilder}
 */
@Getter
public class InventoryBuilder<T extends InventoryItem> implements Cloneable {

    private InventoryConfiguration configuration;

    @Getter(AccessLevel.PROTECTED)
    private Inventory inventory;

    private InventoryProperty properties = new InventoryProperty();

    @Getter
    private Set<InventoryFormat<T>> formats = ConcurrentHashMap.newKeySet();

    @Getter(AccessLevel.PROTECTED)
    private InventoryUpdater<T> updater = InventoryUpdater.of(this);

    @Getter(AccessLevel.PROTECTED)
    private Set<UpdateHandler<T>> updateHandlers = new LinkedHashSet<>();

    /**
     * Create a new InventoryBuilder with the given title and lines.
     *
     * @param title The title
     * @param lines The amount of lines
     */
    public InventoryBuilder(@NotNull String title, int lines) {
        this(new InventoryConfiguration(title, lines));
    }

    /**
     * Create a new InventoryBuilder with the given configuration.
     *
     * @param configuration The InventoryConfiguration
     */
    public InventoryBuilder(@NotNull InventoryConfiguration configuration) {
        Validate.notNull(configuration, "Inventory configuration cannot be null");
        Validate.notNull(configuration.getTitle(), "Inventory title cannot be null");

        if (!InventoryHelper.isEnabled()) {
            throw new InventoryBuilderException("The InventoryHelper must be enabled");
        }

        this.configuration = configuration;
        this.inventory = createInventory();
    }

    /**
     * Create a new InventoryBuilder with the given title and lines.
     *
     * @param title The title
     * @param lines The amount of lines
     * @param <T> The type of InventoryBuilder
     *
     * @return The InventoryBuilder
     */
    @Contract("_, _ -> new")
    public static <T extends InventoryItem> @NotNull InventoryBuilder<T> of(@NotNull String title, int lines) {
        return new InventoryBuilder<T>(title, lines);
    }

    /**
     * Create a new InventoryBuilder with the given configuration.
     *
     * @param configuration The InventoryConfiguration
     * @param <T> The type of InventoryBuilder
     *
     * @return The InventoryBuilder
     */
    @Contract("_ -> new")
    public static <T extends InventoryItem> @NotNull InventoryBuilder<T> of(@NotNull InventoryConfiguration configuration) {
        return new InventoryBuilder<T>(configuration);
    }

    /**
     * Update inventory with given time.
     *
     * @param seconds The seconds to update
     * @param callback The UpdateCallback
     *
     * @return The InventoryBuilder
     */
    @Contract("_,_ -> this")
    public InventoryBuilder<T> withUpdate(long seconds, @NotNull UpdateCallback<T> callback) {
        return withUpdate(seconds, TimeUnit.SECONDS, callback);
    }

    /**
     * Update inventory with given time.
     *
     * @param time The time to update
     * @param unit The TimeUnit of time
     * @param callback The UpdateCallback
     *
     * @return The InventoryBuilder
     */
    public InventoryBuilder<T> withUpdate(long time, @NotNull TimeUnit unit, @NotNull UpdateCallback<T> callback) {
        Validate.isTrue(time > 0, "time must be greater than 0");
        Validate.notNull(unit, "unit cannot be null");
        Validate.notNull(callback, "callback cannot be null");

        this.updateHandlers.add(new UpdateHandler<>(time, unit, callback));
        return this;
    }

    /**
     * Set ItemStack in Inventory
     *
     * @param slot The slot to set
     * @param itemStack The ItemStack to set
     *
     * @return This InventoryBuilder
     */
    public InventoryBuilder<T> withItem(int slot, @NotNull ItemStack itemStack) {
        if (slot >= 0) {
            addFormat(new SimpleItemFormat<>(slot, itemStack, null));
        }

        return this;
    }

    /**
     * Set ItemStack in Inventory
     *
     * @param slot The slot to set
     * @param itemStack The ItemStack to set
     * @param callBack The ItemCallback
     *
     * @return This InventoryBuilder
     */
    public InventoryBuilder<T> withItem(int slot, @NotNull ItemStack itemStack, @NotNull ItemCallback<T> callBack) {
        if (slot >= 0) {
            addFormat(new SimpleItemFormat<>(slot, itemStack, callBack));
        }

        return this;
    }

    /**
     * Set items in Inventory with given pagination configuration
     *
     * @param configuration The PaginatedConfiguration
     * @param items The array of ItemStack
     *
     * @return This InventoryBuilder
     */
    public InventoryBuilder<T> withItems(@NotNull PaginatedConfiguration configuration, @NotNull ItemStack[] items) {
        return withItems(configuration, items, null);
    }

    /**
     * Set items in Inventory with given pagination configuration
     *
     * @param configuration The PaginatedConfiguration
     * @param items The array of ItemStack
     * @param callback The ItemCallback
     *
     * @return This InventoryBuilder
     */
    public InventoryBuilder<T> withItems(@NotNull PaginatedConfiguration configuration, @NotNull ItemStack[] items,
                                         @Nullable PaginatedItemCallback<T> callback) {

        if (configuration.getStart() >= 0) {
            this.addFormat(new PaginatedItemFormat<T>(configuration, items, callback));
        }

        return this;
    }

    /**
     * Set items in Inventory with given pagination configuration
     *
     * @param configuration The PaginatedConfiguration
     * @param items The collection of ItemStack
     *
     * @return This InventoryBuilder
     */
    public InventoryBuilder<T> withItems(@NotNull PaginatedConfiguration configuration, @NotNull Collection<ItemStack> items) {
        return withItems(configuration, items, null);
    }

    /**
     * Set items in Inventory with given pagination configuration
     *
     * @param configuration The PaginatedConfiguration
     * @param items The collection of ItemStack
     * @param callback The PaginatedItemCallback
     *
     * @return This InventoryBuilder
     */
    public InventoryBuilder<T> withItems(@NotNull PaginatedConfiguration configuration, @NotNull Collection<ItemStack> items,
                                         @Nullable PaginatedItemCallback<T> callback) {

        if (configuration.getStart() >= 0) {
            this.addFormat(new PaginatedItemFormat<T>(configuration, items, callback));
        }

        return this;
    }

    /**
     * Set items in Inventory with given object
     *
     * @param slot The slot to set
     * @param value The object to set
     *
     * @return This InventoryBuilder
     */
    public InventoryBuilder<T> withObject(int slot, @NotNull T value) {
        return withObject(slot, value, null);
    }

    /**
     * Set items in Inventory with given object
     *
     * @param slot The slot to set
     * @param value The object to set
     * @param callback The ObjectCallback
     *
     * @return This InventoryBuilder
     */
    public InventoryBuilder<T> withObject(int slot, @NotNull T value, @Nullable ObjectCallback<T> callback) {
        if (slot >= 0) {
            addFormat(new SimpleObjectFormat<>(slot, value, callback));
        }

        return this;
    }

    /**
     * Set items in Inventory with pagination with given objects and configuration
     *
     * @param configuration The PaginatedConfiguration
     * @param objects The array of objects
     * @param callback The PaginatedObjectCallback
     *
     * @return This InventoryBuilder
     */
    public InventoryBuilder<T> withObjects(@NotNull PaginatedConfiguration configuration, @NotNull T[] objects,
                                           @Nullable PaginatedObjectCallback<T> callback) {

        if (configuration.getStart() >= 0) {
            this.addFormat(new PaginatedObjectFormat<T>(configuration, objects, callback));
        }

        return this;
    }

    /**
     * Set items in Inventory with pagination with given objects and configuration
     *
     * @param configuration The PaginatedConfiguration
     * @param objects The collection of objects
     * @param callback The PaginatedObjectCallback
     *
     * @return This InventoryBuilder
     */
    public InventoryBuilder<T> withObjects(@NotNull PaginatedConfiguration configuration, @NotNull Collection<T> objects,
                                           @Nullable PaginatedObjectCallback<T> callback) {

        if (configuration.getStart() >= 0) {
            this.addFormat(new PaginatedObjectFormat<T>(configuration, objects, callback));
        }

        return this;
    }

    /**
     * Add property to Inventory
     *
     * @param key The key of property
     * @param object The object of property
     *
     * @return This InventoryBuilder
     */
    public InventoryBuilder<T> withProperty(@NotNull String key, @NotNull Object object) {
        Validate.notNull(key, "key cannot be null");
        Validate.notNull(object, "object cannot be null");

        this.properties.set(key, object);
        return this;
    }

    /**
     * Set properties of Inventory
     *
     * @param properties The properties.
     *
     * @return This InventoryBuilder
     */
    public InventoryBuilder<T> withProperties(@NotNull InventoryProperty properties) {
        Validate.notNull(properties, "properties cannot be null");
        this.properties = properties;
        return this;
    }

    /**
     * Set a @{@link Button} in inventory.
     *
     * @param button The button.
     *
     * @return This InventoryBuilder.
     */
    public InventoryBuilder<T> withButton(@NotNull Button button) {
        Validate.notNull(button, "button cannot be null");
        addFormat(new ButtonFormatImpl<>(button));
        return this;
    }

    /**
     * Format Inventory
     *
     * @return This InventoryBuilder
     */
    public InventoryBuilder<T> format() {
        inventory.clear();

        for (InventoryFormat<T> format : formats) {
            format.format(inventory, this);
        }

        return this;
    }

    /**
     * Open Inventory to player
     *
     * @param player The Player
     *
     * @return This InventoryBuilder
     */
    public InventoryBuilder<T> open(@NotNull Player player) {
        Validate.notNull(player, "player cannot be null");

        updateInventory();
        player.openInventory(inventory);
        registryUpdates();

        return this;
    }

    /**
     * Build inventory and open it to players.
     *
     * @param players The players
     *
     * @return The Inventory
     */
    public Inventory build(Player @NotNull ... players) {
        Validate.notNull(players, "players cannot be null");
        updateInventory();

        for (Player player : players) {
            player.openInventory(inventory);
        }

        registryUpdates();

        return this.inventory;
    }

    /**
     * Clone this InventoryBuilder
     *
     * @return The cloned InventoryBuilder
     */
    @Override @SuppressWarnings("unchecked")
    public InventoryBuilder<T> clone() {
        try {
            InventoryBuilder<T> clone = (InventoryBuilder<T>) super.clone();

            clone.configuration = this.configuration.clone();
            clone.inventory = clone.createInventory();
            clone.properties = this.properties.clone();
            clone.updater = new InventoryUpdater<>(clone);
            clone.formats = new LinkedHashSet<>(this.formats);
            clone.updateHandlers = new LinkedHashSet<>(this.updateHandlers);

            return clone;

        } catch (Exception exception) {
            throw new InventoryBuilderException(exception);
        }
    }

    protected void addFormat(InventoryFormat<T> format) {
        if (!this.formats.add(format)) {
            this.formats.remove(format);
            this.formats.add(format);
        }
    }

    protected void handleUpdates(int seconds) {
        try {
            for (UpdateHandler<T> handler : updateHandlers) {
                if (handler.canUpdate(seconds)) {
                    handler.call(this.updater);
                }
            }

        } catch (Exception exception) {
            throw new InventoryBuilderException(exception);
        }
    }

    private void updateInventory() {
        format();

        for (HumanEntity human : inventory.getViewers()) {
            if (human instanceof Player) {
                ((Player) human).updateInventory();
            }
        }
    }

    private void registryUpdates() {
        if (this.updateHandlers.size() > 0) {
            InventoryHelper.getManager().register(this);
        }
    }

    private Inventory createInventory() {
        String name = ChatColor.translateAlternateColorCodes('&', configuration.getTitle());
        int size = Math.min(6, Math.max(1, configuration.getLines())) * 9;

        return Bukkit.createInventory(new CustomHolder(event -> {
            if (event instanceof InventoryClickEvent) {
                InventoryClickEvent click = (InventoryClickEvent) event;

                int slot = click.getRawSlot();
                for (InventoryFormat<T> format : formats) {
                    if (format.isValid(slot)) {
                        format.accept(click, this);
                        break;
                    }
                }
            }

        }), size, name);
    }

    @Data
    public static class CustomHolder implements InventoryHolder {

        private final Consumer<InventoryEvent> consumer;

        @Override
        public Inventory getInventory() {
            return null;
        }
    }

}
