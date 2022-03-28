package br.com.blecaute.inventory;

import br.com.blecaute.inventory.callback.ItemCallback;
import br.com.blecaute.inventory.callback.ObjectCallback;
import br.com.blecaute.inventory.enums.ButtonType;
import br.com.blecaute.inventory.exception.InventoryBuilderException;
import br.com.blecaute.inventory.format.InventoryFormat;
import br.com.blecaute.inventory.format.PaginatedFormat;
import br.com.blecaute.inventory.format.impl.PaginatedItemFormat;
import br.com.blecaute.inventory.format.impl.PaginatedObjectFormat;
import br.com.blecaute.inventory.format.impl.SimpleObjectFormat;
import br.com.blecaute.inventory.format.impl.SimpleItemFormat;
import br.com.blecaute.inventory.property.InventoryProperty;
import br.com.blecaute.inventory.type.InventoryItem;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A simple class for building of @{@link Inventory}.
 * @param <T> The type of @{@link InventoryBuilder}
 */
@Getter
public class InventoryBuilder<T extends InventoryItem> implements Cloneable {

    @Getter(AccessLevel.NONE) private final String inventoryName;
    @Getter(AccessLevel.NONE) private Inventory inventory;

    @Getter(AccessLevel.NONE) private Function<Integer, Boolean> skipFunction;

    private int startSlot = 0;
    private int exitSlot;

    private int pageSize = 0;
    private int currentPage = 1;

    private InventoryProperty properties = new InventoryProperty();
    private Map<ButtonType, Pair<Integer, ItemStack>> buttons = new EnumMap<>(ButtonType.class);
    private Set<InventoryFormat<T>> formats = new LinkedHashSet<>();

    /**
     * Create instance of @{@link InventoryBuilder}
     *
     * @param name  The name of @{@link Inventory}
     * @param lines The lines of @{@link Inventory}
     */
    public InventoryBuilder(String name, int lines) {
        if (!InventoryHelper.isEnabled()) {
            throw new InventoryBuilderException("The InventoryHelper must be enabled");
        }

        int size = Math.min(6, Math.max(1, lines)) * 9;
        this.inventoryName = name.replace("&", "§");
        this.exitSlot = size;
        this.inventory = createInventory(size);
    }

    /**
     * Set number of objects on each page.
     *
     * @param size  The size
     * @return This @{@link InventoryBuilder}
     */
    public InventoryBuilder<T> withPageSize(int size)  {
        this.pageSize = size;
        return this;
    }

    /**
     * Set slot to start the place of items.
     *
     * @param start The slot
     * @return This @{@link InventoryBuilder}
     */
    public InventoryBuilder<T> withStart(int start) {
        this.startSlot = start;
        return this;
    }

    /**
     * Set slot to stop place of items.
     *
     * @param exit  The slot.
     * @return This @{@link InventoryBuilder}
     */
    public InventoryBuilder<T> withExit(int exit) {
        this.exitSlot = exit;
        return this;
    }

    /**
     * Skip placing items in these slots.
     *
     * @param skip The slots
     * @return This @{@link InventoryBuilder}
     */
    public InventoryBuilder<T> withSkip(int... skip) {
        this.skipFunction = integer -> Arrays.stream(skip).anyMatch(slot -> slot == integer);
        return this;
    }

    /**
     * Skip placing items in these slots.
     *
     * @param skip The @{@link Function} to check slot.
     * @return This @{@link InventoryBuilder}
     */
    public InventoryBuilder<T> withSkip(@Nullable Function<Integer, Boolean> skip) {
        this.skipFunction = skip;
        return this;
    }

    /**
     * Set item in @{@link Inventory}
     *
     * @param slot      The slot
     * @param itemStack The @{@link ItemStack}
     * @param callBack  The @{@link ItemCallback}
     *
     * @return This @{@link InventoryBuilder}
     */
    public InventoryBuilder<T> withItem(int slot, @NotNull ItemStack itemStack, @Nullable ItemCallback<T> callBack) {

        if (slot >= 0) {
            addFormat(new SimpleItemFormat<>(slot, itemStack, callBack));
        }

        return this;
    }

    /**
     * Set items in @{@link Inventory} with pagination
     *
     * @param items The array of @{@link ItemStack}
     * @param callBack The @{@link ItemCallback}
     * @return This @{@link InventoryBuilder}
     */
    public InventoryBuilder<T> withItems(@NotNull ItemStack[] items, @Nullable ItemCallback<T> callBack) {
        return withItems(Arrays.asList(items), callBack);
    }

    /**
     * Set items in @{@link Inventory} with pagination
     *
     * @param items The collection of @{@link ItemStack}
     * @param callBack The @{@link ItemCallback}
     * @return This @{@link InventoryBuilder}
     */
    public InventoryBuilder<T> withItems(@NotNull Collection<ItemStack> items, @Nullable ItemCallback<T> callBack) {
        return withItems(new ArrayList<>(items), callBack);
    }

    /**
     * Set items in @{@link Inventory} with pagination
     *
     * @param items     The list of @{@link ItemStack}
     * @param callBack  The @{@link ItemCallback}
     *
     * @return This @{@link InventoryBuilder}
     */
    public InventoryBuilder<T> withItems(@NotNull List<ItemStack> items, @Nullable ItemCallback<T> callBack) {
        addFormat(new PaginatedItemFormat<>(items, skipFunction, callBack));
        return this;
    }

    /**
     * Set item in @{@link Inventory} with @{@link InventoryItem}
     *
     * @param slot      The slot
     * @param value     The @{@link InventoryItem}
     * @param callBack  The @{@link ItemCallback}
     *
     * @return This @{@link InventoryBuilder}
     */
    public InventoryBuilder<T> withObject(int slot, @NotNull T value, @Nullable ObjectCallback<T> callBack) {

        if (slot >= 0) {
            addFormat(new SimpleObjectFormat<>(slot, value, callBack));
        }

        return this;
    }

    /**
     * Set items in @{@link Inventory} with pagination
     *
     * @param objects The array of @{@link InventoryItem}
     * @param callBack The @{@link ObjectCallback}
     * @return This @{@link InventoryBuilder}
     */
    public InventoryBuilder<T> withObjects(@NotNull T[] objects, @Nullable ObjectCallback<T> callBack) {
        return withObjects(Arrays.asList(objects), callBack);
    }

    /**
     * Set items in @{@link Inventory} with pagination
     *
     * @param objects The collection of @{@link InventoryItem}
     * @param callBack The @{@link ObjectCallback}
     * @return This @{@link InventoryBuilder}
     */
    public InventoryBuilder<T> withObjects(@NotNull Collection<T> objects, @Nullable ObjectCallback<T> callBack) {
        return withObjects(new ArrayList<>(objects), callBack);
    }

    /**
     * Set items in @{@link Inventory} with pagination
     *
     * @param objects   The list of objects
     * @param callBack  The @{@link ObjectCallback}
     *
     * @return This @{@link InventoryBuilder}
     */
    public InventoryBuilder<T> withObjects(@NotNull List<T> objects, @Nullable ObjectCallback<T> callBack) {
        addFormat(new PaginatedObjectFormat<>(objects, skipFunction, callBack));
        return this;
    }

    /**
     * Set @{@link ButtonType}
     *
     * @param type      The @{@link ButtonType}
     * @param slot      The slot
     * @param itemStack The @{@link ItemStack}
     *
     * @return This @{@link InventoryBuilder}
     */
    public InventoryBuilder<T> withButton(@NotNull ButtonType type, int slot, @NotNull ItemStack itemStack) {
        buttons.put(type, Pair.of(slot, itemStack));
        return this;
    }

    /**
     * Add property to @{@link InventoryBuilder}
     *
     * @param key       The key.
     * @param object    The object.
     *
     * @return This @{@link InventoryBuilder}
     */
    public InventoryBuilder<T> withProperty(@NotNull String key, @NotNull Object object) {
        this.properties.set(key, object);
        return this;
    }

    /**
     * Set properties of @{@link InventoryBuilder}
     *
     * @param properties The properties.
     *
     * @return This @{@link InventoryBuilder}
     */
    public InventoryBuilder<T> withProperties(@NotNull InventoryProperty properties) {
        this.properties = properties;
        return this;
    }

    /**
     * Clone @{@link InventoryBuilder}
     * @return The clone of this @{@link InventoryBuilder}
     */
    @Override @SuppressWarnings("unchecked")
    public InventoryBuilder<T> clone() {
        try {
            InventoryBuilder<T> clone = (InventoryBuilder<T>) super.clone();

            clone.inventory = clone.createInventory(this.inventory.getSize());
            clone.properties = this.properties.clone();
            clone.buttons = new EnumMap<>(this.buttons);
            clone.formats = new LinkedHashSet<>(this.formats);

            return clone;

        } catch (Exception exception) {
            throw new InventoryBuilderException(exception);
        }
    }

    public InventoryBuilder<T> withFormat(@NotNull InventoryFormat<T> format) {
        addFormat(format);
        return this;
    }

    /**
     * Format @{@link Inventory}
     * @return This @{@link InventoryBuilder}
     */
    public InventoryBuilder<T> format() {
        inventory.clear();

        for (InventoryFormat<T> format : formats) {

            if (format instanceof PaginatedFormat) {
                PaginatedFormat<T> paginated = (PaginatedFormat<T>) format;
                paginated.format(inventory, this);

                createPages(paginated.getSize());
                continue;
            }

            format.format(inventory, this);
        }

        return this;
    }

    /**
     * Open @{@link Inventory} to player
     *
     * @param player The @{@link Player}
     *
     * @return This @{@link InventoryBuilder}
     */
    public InventoryBuilder<T> open(Player player) {
        updateInventory();
        player.openInventory(inventory);

        return this;
    }

    /**
     * Build inventory and open it to players.
     *
     * @param players he @{@link Player}
     *
     * @return The @{@link Inventory}
     */
    public Inventory build(Player... players) {
        updateInventory();

        for (Player player : players) {
            player.openInventory(inventory);
        }

        return this.inventory;
    }

    private void addFormat(InventoryFormat<T> format) {
        if (!this.formats.add(format)) {
            this.formats.remove(format);
            this.formats.add(format);
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

    private void createPages(int size) {
        if(this.currentPage > 1 && buttons.containsKey(ButtonType.PREVIOUS_PAGE)) {
            Pair<Integer, ItemStack> pair = buttons.get(ButtonType.PREVIOUS_PAGE);
            inventory.setItem(pair.getKey(), pair.getValue());
        }

        if(this.currentPage > 0 && this.pageSize > 0 &&
                buttons.containsKey(ButtonType.NEXT_PAGE) &&
                size > this.currentPage * this.pageSize) {

            Pair<Integer, ItemStack> pair = buttons.get(ButtonType.NEXT_PAGE);
            inventory.setItem(pair.getKey(), pair.getValue());
        }
    }

    private Inventory createInventory(int size) {
        return Bukkit.createInventory(new CustomHolder(event -> {
            if (event instanceof org.bukkit.event.inventory.InventoryClickEvent) {
                org.bukkit.event.inventory.InventoryClickEvent click = (org.bukkit.event.inventory.InventoryClickEvent) event;

                int slot = click.getRawSlot();

                for (Map.Entry<ButtonType, Pair<Integer, ItemStack>> entry : buttons.entrySet()) {
                    if (entry.getValue().getKey() == slot) {
                        this.currentPage = this.currentPage + entry.getKey().getValue();
                        format();
                        return;
                    }
                }

                for (InventoryFormat<T> format : formats) {
                    if (format.isValid(slot)) {
                        format.accept(click, this);
                        break;
                    }
                }

            }

        }), size, inventoryName);
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