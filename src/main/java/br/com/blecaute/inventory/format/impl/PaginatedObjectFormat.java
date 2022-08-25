package br.com.blecaute.inventory.format.impl;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.buttons.Button;
import br.com.blecaute.inventory.callback.ItemCallback;
import br.com.blecaute.inventory.callback.ObjectCallback;
import br.com.blecaute.inventory.callback.PaginatedObjectCallback;
import br.com.blecaute.inventory.configuration.PaginatedConfiguration;
import br.com.blecaute.inventory.event.PaginatedObjectEvent;
import br.com.blecaute.inventory.format.PaginatedFormat;
import br.com.blecaute.inventory.format.updater.PaginatedObjectUpdater;
import br.com.blecaute.inventory.type.InventoryItem;
import br.com.blecaute.inventory.type.InventorySlot;
import br.com.blecaute.inventory.validator.SlotInvalidator;
import org.apache.commons.lang.Validate;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PaginatedObjectFormat<T extends InventoryItem> implements PaginatedFormat<T>, PaginatedObjectUpdater<T> {

    private PaginatedConfiguration configuration;
    private PaginatedObjectCallback<T> callback;

    private final List<SimpleObjectFormat<T>> items = new ArrayList<>();

    private final Map<Integer, SimpleObjectFormat<T>> slots = new HashMap<>();
    private final Map<Integer, List<SimpleObjectFormat<T>>> pages = new HashMap<>();
    private final Map<Integer, Button> buttons = new HashMap<>();

    private int currentPage = 1;

    public PaginatedObjectFormat(@NotNull PaginatedConfiguration configuration, @NotNull Collection<T> items,
                                 @Nullable PaginatedObjectCallback<T> callback) {

        validateConstructor(configuration, items, callback);

        for (T item : items) {
            this.items.add(new SimpleObjectFormat<>(-1, item, null));
        }
    }

    public PaginatedObjectFormat(@NotNull PaginatedConfiguration configuration, @NotNull T[] items,
                                 @Nullable PaginatedObjectCallback<T> callback) {

        validateConstructor(configuration, items, callback);

        for (T item : items) {
            this.items.add(new SimpleObjectFormat<>(-1, item, null));
        }
    }

    private void validateConstructor(@NotNull PaginatedConfiguration configuration, @NotNull Object items,
                                     @Nullable PaginatedObjectCallback<T> callback) {

        Validate.notNull(configuration, "configuration cannot be null");
        Validate.notNull(items, "items cannot be null");

        this.configuration = configuration;
        this.callback = callback;
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

        SimpleObjectFormat<T> format = slots.get(slot);
        if (format == null) return;

        PaginatedObjectCallback<T> callback = this.callback;
        if (format.getCallBack() != null) {
            callback = click -> format.getCallBack().accept(click);
        }

        if (callback != null) {
            callback.accept(new PaginatedObjectEvent<>(this, builder, event, format.getObject()));
        }
    }

    @Override
    public PaginatedConfiguration getConfiguration() {
        return this.configuration;
    }

    @Override
    public int getSize() {
        return this.items.size();
    }

    @Override
    public int getCurrentPage() {
        return this.currentPage;
    }

    @Override
    public void setCurrentPage(@NotNull Inventory inventory, @NotNull InventoryBuilder<T> builder, int page) {
        Validate.isTrue(page > 0 && page <= getSize(), "the page must be between 1 and " + this.getPages());
        validate(builder, inventory);

        this.currentPage = page;
        this.clearInventory(inventory);
        this.format(inventory, builder);
    }

    @Override
    public int getPages() {
        return this.pages.size();
    }

    @Override
    public void format(@NotNull Inventory inventory, @NotNull InventoryBuilder<T> builder) {
        validate(builder, inventory);

        slots.clear();
        buttons.clear();

        int size = configuration.getSize();
        int start = configuration.getStart();
        int exit = configuration.getEnd();

        calculate(size, this.items, this.pages);

        if (exit <= 0 || exit >= inventory.getSize()) {
            exit = inventory.getSize() - 1;
        }

        List<SimpleObjectFormat<T>> values = this.pages.get(this.getCurrentPage());
        if (values == null) return;

        SlotInvalidator validator = configuration.getValidator();
        for (int index = 0; index < values.size() && start < exit; start++) {

            SimpleObjectFormat<T> format = values.get(index);
            if (format.getSlot() >= 0) {
                inventory.setItem(start, format.getItemStack(inventory, builder));
                slots.put(start, format);
                index++;
                continue;
            }

            T value = format.getObject();
            if (value instanceof InventorySlot) {
                InventorySlot inventorySlot = (InventorySlot) value;

                int itemSlot = inventorySlot.getSlot();
                if (itemSlot > 0) {
                    inventory.setItem(itemSlot, format.getItemStack(inventory, builder));
                    slots.put(itemSlot, format);
                }

                index++;
                continue;
            }

            if (validator != null && validator.validate(start)) continue;

            inventory.setItem(start, format.getItemStack(inventory, builder));
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

        SimpleObjectFormat<T> format = slots.get(slot);
        if (format != null) {
            format.setIcon(itemStack);
            inventory.setItem(slot, format.getItemStack(inventory, builder));
            return;
        }

        List<SimpleObjectFormat<T>> formats = pages.get(this.getCurrentPage());
        if (formats != null) {
            format = new SimpleObjectFormat<>(slot, itemStack, null, null);
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

        SimpleObjectFormat<T> format = slots.get(slot);
        if (format != null) {
            format.setCallBack(click -> {
                if (callback != null) {
                    callback.accept(click);
                }
            });
        }
    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory,
                       @NotNull T object, int slot) {

        validate(builder, inventory);
        Validate.notNull(object, "object cannot be null");

        SimpleObjectFormat<T> format = slots.get(slot);
        if (format != null) {
            format.setObject(object);
            format.setIcon(null);

            inventory.setItem(slot, format.getItemStack(inventory, builder));
            return;
        }

        List<SimpleObjectFormat<T>> formats = pages.get(this.getCurrentPage());
        if (formats != null) {
            format = new SimpleObjectFormat<>(slot, object, null);
            formats.add(format);

            inventory.setItem(slot, format.getItemStack(inventory, builder));
            slots.put(slot, format);
        }
    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory,
                       @Nullable ObjectCallback<T> callback, @NotNull T object, int slot) {

        update(builder, inventory, object, slot);

        SimpleObjectFormat<T> format = slots.get(slot);
        if (format != null) {
            format.setCallBack(callback);
        }
    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory, @NotNull Collection<T> objects) {
        validate(builder, inventory);
        clearInventory(inventory);

        for (T item : objects) {
            this.items.add(new SimpleObjectFormat<>(-1, item, null));
        }

        format(inventory, builder);
    }


    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory,
                       @NotNull Collection<T> objects, @Nullable PaginatedObjectCallback<T> callback) {

        update(builder, inventory, objects);

        this.callback = callback;
    }

    private void validate(InventoryBuilder<T> builder, Inventory inventory) {
        Validate.notNull(builder, "builder cannot be null");
        Validate.notNull(inventory, "inventory cannot be null");
    }

    private void clearInventory(Inventory inventory) {
        this.items.clear();
        this.pages.clear();

        for (Map.Entry<Integer, SimpleObjectFormat<T>> entry : this.slots.entrySet()) {
            inventory.setItem(entry.getKey(), null);
        }

        for (Map.Entry<Integer, Button> entry : this.buttons.entrySet()) {
            inventory.setItem(entry.getKey(), null);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaginatedObjectFormat)) return false;

        PaginatedObjectFormat<?> that = (PaginatedObjectFormat<?>) o;
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
