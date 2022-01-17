package br.com.blecaute.inventory;

import br.com.blecaute.inventory.exception.InventoryBuilderException;
import br.com.blecaute.inventory.format.InventoryFormat;
import br.com.blecaute.inventory.format.impl.SimpleObjectFormat;
import br.com.blecaute.inventory.format.impl.SimpleItemFormat;
import br.com.blecaute.inventory.type.InventoryItemType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * class to facilitate the construction of inventories.
 * @param <T> the type of builder
 */
public class InventoryBuilder<T extends InventoryItemType> implements Cloneable {

    @Getter private final String inventoryName;
    @Getter private Inventory inventory;

    @Getter private int page = 1;
    @Getter private int size = 0;

    private int exit;
    private int start = 0;
    private int value = 0;

    private Function<Integer, Boolean> scape;

    private Map<Object, Object> DATA = new HashMap<>();
    private Map<ButtonType, Pair<Integer, ItemStack>> PAGES = new HashMap<>();
    private List<InventoryFormat<T>> FORMATS = new ArrayList<>();

    /**
     * Default constructor
     *
     * @param name  the name of inventory
     * @param lines lines of inventory
     */
    public InventoryBuilder(String name, int lines) {
        int size = Math.min(6, Math.max(1, lines)) * 9;
        this.inventoryName = name.replace("&", "§");
        this.exit = size - 1;
        this.inventory = createInventory(size);
    }

    /**
     * Build inventory
     *
     * @return the @{@link Inventory}
     */
    public Inventory build() {
        formatInventory();
        return this.inventory;
    }

    /**
     * Set first page of inventory and size of objects in the pages
     *
     * @param page  the page
     * @param size  the size
     * @return this
     */
    public InventoryBuilder<T> withPage(int page, int size)  {
        this.page = page;
        this.size = size;

        return this;
    }

    /**
     * Set slot to start the place of items.
     *
     * @param start the slot
     * @return this
     */
    public InventoryBuilder<T> withSlotStart(int start) {
        this.start = start;
        return this;
    }

    /**
     * Skip slots and add to the current slot
     *
     * @param value the value to be added
     * @param scape the slots to skip
     * @return this
     */
    public InventoryBuilder<T> withSlotSkip(int value, int... scape) {
        this.value = value;
        this.scape = integer -> Arrays.stream(scape).anyMatch(slot -> slot == integer);

        return this;
    }

    /**
     * skip slots and add to the current slot
     *
     * @param value the value to be added
     * @param scape the function to skip
     * @return the builder
     */
    public InventoryBuilder<T> withSlotSkip(int value, Function<Integer, Boolean> scape) {
        this.value = value;
        this.scape = scape;

        return this;
    }

    /**
     * slot to stop setting items
     *
     * @param exit the exit
     * @return the builder
     */
    public InventoryBuilder<T> withSlotExit(int exit) {
        this.exit = exit;
        return this;
    }

    /**
     * set the item of inventory
     *
     * @param slot the slot
     * @param itemStack the ItemStack
     * @param consumer the consumer
     * @return the builder
     */
    public InventoryBuilder<T> withItem(int slot, ItemStack itemStack, Consumer<InventoryClick<T>> consumer) {

        if (slot > 0) {
            FORMATS.add(new SimpleItemFormat<>(slot, itemStack, consumer));
        }

        return this;
    }

    /**
     * set the item of inventory with parameter T
     *
     * @param slot the slot
     * @param value the parameter
     * @param consumer the consumer
     * @return the builder
     */
    public InventoryBuilder<T> withObject(int slot, T value, Consumer<InventoryClick<T>> consumer) {

        if (slot > 0) {
            FORMATS.add(new SimpleObjectFormat<>(slot, value, consumer));
        }

        return this;
    }


    /**
     * set the item to skip the inventory pages
     *
     * @param slot the slot
     * @param itemStack the item
     * @return the builder
     */
    public InventoryBuilder<T> withNextPage(int slot, ItemStack itemStack) {
        PAGES.put(ButtonType.NEXT, Pair.of(slot, itemStack));
        return this;
    }

    /**
     * set the item to return the inventory pages
     *
     * @param slot the slot of item
     * @param itemStack the item
     * @return the builder
     */
    public InventoryBuilder<T> withBackPage(int slot, ItemStack itemStack) {
        PAGES.put(ButtonType.BACK, Pair.of(slot, itemStack));
        return this;
    }

    /**
     * open inventory to player
     *
     * @param player the player
     * @return the builder
     */
    public InventoryBuilder<T> open(Player player) {
        player.openInventory(this.inventory);
        formatInventory();
        player.updateInventory();

        return this;
    }

    /**
     * Add data to inventory
     *
     * @param key The key of data
     * @param value The value of data
     * @return The @{@link InventoryBuilder}
     */
    public InventoryBuilder<T> withData(Object key, Object value) {
        DATA.put(key, value);
        return this;
    }

    /**
     * Get data of @{@link InventoryBuilder}
     * @param key The key
     * @return The value
     */
    @Nullable
    public Object getData(Object key) {
        return DATA.get(key);
    }

    /**
     * Remove data from builder
     * @param key The key
     * @return The @{@link InventoryBuilder}
     */
    public InventoryBuilder<T> removeData(Object key) {
        DATA.remove(key);
        return this;
    }

    /**
     * Clone this builder.
     *
     * @return the clone of this builder.
     */
    @Override @SuppressWarnings("unchecked")
    public InventoryBuilder<T> clone() {
        try {
            InventoryBuilder<T> clone = (InventoryBuilder<T>) super.clone();

            clone.inventory = clone.createInventory(this.inventory.getSize());

            clone.DATA = new HashMap<>(this.DATA);
            clone.PAGES = new HashMap<>(this.PAGES);
            clone.FORMATS = new ArrayList<>(this.FORMATS);

            return clone;

        } catch (CloneNotSupportedException exception) {
            throw new InventoryBuilderException(exception);
        }

    }

    /**
     * Format the inventory and update items
     * @return the builder
     */
    @SuppressWarnings("ALL")
    public InventoryBuilder<T> formatInventory() {
        inventory.clear();

        for (InventoryFormat<T> format : FORMATS) {

        }

      /*  FORMATS.forEach(format -> {
            if(format instanceof InventoryBuilder.MultiValueInventoryFormat) {
                MultiValueInventoryFormat value = (MultiValueInventoryFormat) format;
                value.map.clear();

                int slot = this.start;

                List<T> items = size <= 0 ? value.items : ListUtil.getSublist(value.items, page, size);
                for(int index = 0; index < items.size(); slot++) {
                    T item = items.get(index);

                    if(item instanceof InventorySlot) {
                        InventorySlot slotItem = (InventorySlot) item;
                        if (slotItem.getSlot() >= 0) {
                            inventory.setItem(slotItem.getSlot(), slotItem.getItem(this.inventory, this));
                            value.map.put(slotItem.getSlot(), item);
                        }

                        index++;
                        continue;
                    }

                    if(slot > this.exit) {
                        break;
                    }

                    if(this.scape != null && this.scape.apply(slot)) {
                        slot += this.value - 1;
                        continue;
                    }

                    inventory.setItem(slot, item.getItem(this.inventory, this));
                    value.map.put(slot, item);

                    index++;
                }

                createPages(value.items.size());

            } else if (format instanceof InventoryBuilder.SingleInventoryFormat) {
                SingleInventoryFormat singleFormat = (SingleInventoryFormat) format;
                if (singleFormat.slot >= 0) {
                    inventory.setItem(singleFormat.slot, singleFormat.itemStack);
                }

            } else if (format instanceof InventoryBuilder.MultiItemInventoryFormat) {
                MultiItemInventoryFormat value = (MultiItemInventoryFormat) format;
                value.map.clear();

                int slot = this.start;

                List<ItemStack> items = size <= 0 ? value.items : ListUtil.getSublist(value.items, page, size);
                for(int index = 0; index < items.size(); slot++) {
                    if(slot > this.exit) {
                        break;
                    }

                    if(this.scape != null && this.scape.apply(slot)) {
                        slot += this.value - 1;
                        continue;
                    }

                    ItemStack item = items.get(index);
                    inventory.setItem(slot, item);
                    value.map.put(slot, item);

                    index++;
                }

                createPages(value.items.size());
            }
        });
*/
        return this;
    }

    /**
     * Create a pages of inventory
     * @param size the size of list
     */
    private void createPages(int size) {
        if(this.page > 1 && PAGES.containsKey(ButtonType.BACK)) {
            Pair<Integer, ItemStack> pair = PAGES.get(ButtonType.BACK);
            inventory.setItem(pair.getKey(), pair.getValue());
        }

        if(this.size > 0 && PAGES.containsKey(ButtonType.NEXT) && size > this.page * this.size) {
            Pair<Integer, ItemStack> pair = PAGES.get(ButtonType.NEXT);
            inventory.setItem(pair.getKey(), pair.getValue());
        }
    }

    private Inventory createInventory(int size) {
        return Bukkit.createInventory(new CustomInventoryHolder(event -> {
            if (event instanceof InventoryClickEvent) {
                InventoryClickEvent click = (InventoryClickEvent) event;

                int slot = click.getRawSlot();

                for(Map.Entry<ButtonType, Pair<Integer, ItemStack>> entry : PAGES.entrySet()) {
                    if(entry.getValue().getKey() == slot) {
                        this.page = this.page + entry.getKey().value;
                        formatInventory();
                        return;
                    }
                }

/*            for (InventoryFormat<T> format : FORMATS) {
                if (format.isValid(event.getRawSlot())) {
                    format.accept(event);
                    break;
                }
            }*/
            }

        }), size, inventoryName);
    }

    /**
     * Private enum of buttons to easy
     * skip and back pages of the inventory
     */
    @AllArgsConstructor
    private enum ButtonType {

        BACK(-1),
        NEXT(1);

        private final int value;
    }
}