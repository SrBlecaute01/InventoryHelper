package br.com.blecaute.inventory.format.impl;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.button.Button;
import br.com.blecaute.inventory.callback.ItemCallback;
import br.com.blecaute.inventory.callback.PaginatedItemCallback;
import br.com.blecaute.inventory.configuration.PaginatedConfiguration;
import br.com.blecaute.inventory.event.PaginatedItemEvent;
import br.com.blecaute.inventory.format.PaginatedFormat;
import br.com.blecaute.inventory.format.updater.PaginatedItemUpdater;
import br.com.blecaute.inventory.type.InventoryItem;
import br.com.blecaute.inventory.validator.SlotInvalidator;
import org.apache.commons.lang.Validate;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PaginatedItemFormat<T extends InventoryItem> implements PaginatedFormat<T>, PaginatedItemUpdater<T> {

    private PaginatedConfiguration configuration;
    private PaginatedItemCallback<T> callback;

    private final List<SimpleItemFormat<T>> items = new ArrayList<>();

    private final Map<Integer, SimpleItemFormat<T>> slots = new HashMap<>();
    private final Map<Integer, List<SimpleItemFormat<T>>> pages = new HashMap<>();
    private final Map<Integer, Button> buttons = new HashMap<>();

    private int currentPage = 1;

    public PaginatedItemFormat(@NotNull PaginatedConfiguration configuration, @NotNull Collection<ItemStack> items,
                               @Nullable PaginatedItemCallback<T> callback) {

        validateConstructor(configuration, items, callback);

        for (ItemStack item : items) {
            this.items.add(new SimpleItemFormat<>(-1, item, null));
        }
    }

    public PaginatedItemFormat(@NotNull PaginatedConfiguration configuration, @NotNull ItemStack[] items,
                               @Nullable PaginatedItemCallback<T> callback) {

        validateConstructor(configuration, items, callback);

        for (ItemStack item : items) {
            this.items.add(new SimpleItemFormat<>(-1, item, null));
        }
    }

    private void validateConstructor(PaginatedConfiguration configuration, Object items, PaginatedItemCallback<T> callBack) {
        Validate.notNull(configuration, "configuration cannot be null");
        Validate.notNull(items, "items cannot be null");

        this.configuration = configuration;
        this.callback = callBack;
    }

    @Override
    public boolean isValid(int slot) {
        return buttons.containsKey(slot) || slots.containsKey(slot);
    }

    @Override
    public void accept(@NotNull InventoryClickEvent event, @NotNull InventoryBuilder<T> builder) {
        Validate.notNull(event, "event cannot be null");
        Validate.notNull(builder, "builder cannot be null");

        int slot = event.getRawSlot();

        Button button = this.buttons.get(slot);
        if (button != null) {
            button.accept(event, builder, this);
            return;
        }

        SimpleItemFormat<T> format = slots.get(slot);
        if (format == null) return;

        PaginatedItemCallback<T> callback = this.callback;
        if (format.getCallBack() != null) {
            callback = click -> format.getCallBack().accept(click);
        }

        if (callback != null) {
            callback.accept(new PaginatedItemEvent<>(this, builder, event));
        }
    }

    @Override
    public PaginatedConfiguration getConfiguration() {
        return this.configuration;
    }

    @Override
    public int getSize() {
        return items.size();
    }

    @Override
    public int getPages() {
        return pages.size();
    }

    @Override
    public int getCurrentPage() {
        return this.currentPage;
    }

    @Override
    public void setCurrentPage(@NotNull Inventory inventory, @NotNull InventoryBuilder<T> builder, int page) {
        Validate.isTrue(page > 0 && page <= this.getPages(), "page must be between 1 and " + this.getPages());
        validate(builder, inventory);

        this.currentPage = page;
        this.clearInventory(inventory);
        this.format(inventory, builder);
    }

    @Override
    public void format(@NotNull Inventory inventory, @NotNull InventoryBuilder<T> builder) {
        validate(builder, inventory);

        int start = configuration.getStart();
        int exit = configuration.getEnd();
        int size = configuration.getSize();

        calculate(size, this.items, this.pages);

        slots.clear();
        buttons.clear();

        if (exit <= 0 || exit >= inventory.getSize()) {
            exit = inventory.getSize() - 1;
        }

        List<SimpleItemFormat<T>> values = this.pages.get(this.getCurrentPage());
        if (values == null) return;

        SlotInvalidator validator = configuration.getValidator();
        for (int index = 0; index < values.size() && start < exit; start++) {
            SimpleItemFormat<T> format = values.get(index);
            if (format.getSlot() >= 0) {
                inventory.setItem(start, format.getItemStack());
                slots.put(start, format);
                index++;

                continue;
            }

            if (validator != null && validator.validate(start)) continue;

            ItemStack item = format.getItemStack();
            inventory.setItem(start, item);

            slots.put(start, format);
            index++;
        }

        for (Button button : configuration.getButtons()) {
            if (button.canPlace(builder, this)) {
                int slot = button.getSlot();

                inventory.setItem(slot, button.getItem(inventory, builder.getProperties()));
                buttons.put(slot, button);
            }
        }
    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory,
                       @Nullable ItemStack itemStack, int slot) {

        validate(builder, inventory);

        SimpleItemFormat<T> format = slots.get(slot);
        if (format != null) {
            format.setItemStack(itemStack);
            inventory.setItem(slot, format.getItemStack(inventory, builder));
            return;
        }

        List<SimpleItemFormat<T>> formats = pages.get(this.getCurrentPage());
        if (formats != null) {
            format = new SimpleItemFormat<>(slot, itemStack, null);
            formats.add(format);

            inventory.setItem(slot, format.getItemStack(inventory, builder));
            slots.put(slot, format);
        }
    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory,
                       @Nullable ItemCallback<T> callback, @Nullable ItemStack itemStack,
                       int slot) {

        update(builder, inventory, itemStack, slot);

        SimpleItemFormat<T> format = slots.get(slot);
        if (format != null) {
            format.setCallBack(callback);
        }
    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory, @NotNull Collection<ItemStack> items) {
        validate(builder, inventory);
        clearInventory(inventory);

        for (ItemStack item : items) {
            this.items.add(new SimpleItemFormat<>(-1, item, null));
        }

        format(inventory, builder);
    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory,
                       @NotNull Collection<ItemStack> items, @Nullable PaginatedItemCallback<T> callback) {

        update(builder, inventory, items);
        this.callback = callback;
    }

    private void validate(InventoryBuilder<T> builder, Inventory inventory) {
        Validate.notNull(builder, "builder cannot be null");
        Validate.notNull(inventory, "inventory cannot be null");
    }

    private void clearInventory(Inventory inventory) {
        this.items.clear();
        this.pages.clear();

        for (Map.Entry<Integer, SimpleItemFormat<T>> entry : this.slots.entrySet()) {
            inventory.setItem(entry.getKey(), null);
        }

        for (Map.Entry<Integer, Button> entry : this.buttons.entrySet()) {
            inventory.setItem(entry.getKey(), null);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaginatedItemFormat)) return false;

        PaginatedItemFormat<?> that = (PaginatedItemFormat<?>) o;
        return Objects.equals(getConfiguration(), that.getConfiguration()) &&
                Objects.equals(callback, that.callback) &&
                Objects.equals(items, that.items) &&
                Objects.equals(slots, that.slots);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getConfiguration(), callback, items, slots);
    }
}
