package br.com.blecaute.inventory.format.impl;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.button.Button;
import br.com.blecaute.inventory.callback.ItemCallback;
import br.com.blecaute.inventory.callback.ObjectCallback;
import br.com.blecaute.inventory.callback.PaginatedObjectCallback;
import br.com.blecaute.inventory.configuration.PaginatedConfiguration;
import br.com.blecaute.inventory.event.PaginatedObjectEvent;
import br.com.blecaute.inventory.format.ButtonFormat;
import br.com.blecaute.inventory.format.InventoryFormat;
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

public class PaginatedObjectFormatImpl<T extends InventoryItem> implements PaginatedFormat<T>, PaginatedObjectUpdater<T> {

    private PaginatedConfiguration configuration;
    private PaginatedObjectCallback<T> callback;

    private final List<SimpleObjectFormatImpl<T>> items = new ArrayList<>();
    private final List<ButtonFormatImpl<T>> buttons = new ArrayList<>();

    private final Map<Integer, InventoryFormat<T>> slots = new HashMap<>();
    private final Map<Integer, List<SimpleObjectFormatImpl<T>>> pages = new HashMap<>();

    private int currentPage = 1;

    public PaginatedObjectFormatImpl(@NotNull PaginatedConfiguration configuration,
                                     @NotNull Collection<? extends T> items,
                                     @Nullable PaginatedObjectCallback<T> callback) {

        validateConstructor(configuration, items, callback);

        for (T item : items) {
            this.items.add(new SimpleObjectFormatImpl<>(-1, item, null));
        }

        for (Button button : configuration.getButtons()) {
            this.buttons.add(new ButtonFormatImpl<>(button));
        }
        
        calculate(configuration.getSize(), this.items, this.pages);
    }

    @Override
    public boolean isValid(int slot) {
        return slots.containsKey(slot);
    }

    @Override
    public void accept(@NotNull InventoryClickEvent event, @NotNull InventoryBuilder<T> builder) {
        Validate.notNull(event, "event cannot be null");
        Validate.notNull(builder, "builder cannot be null");

        InventoryFormat<T> format = slots.get(event.getRawSlot());
        if (format == null) return;

        if (format instanceof ButtonFormat) {
            ButtonFormat<T> buttonFormat = (ButtonFormat<T>) format;
            buttonFormat.accept(event, builder, this);
            return;
        }

        if (format instanceof SimpleObjectFormatImpl) {
            SimpleObjectFormatImpl<T> objectFormat = (SimpleObjectFormatImpl<T>) format;
            PaginatedObjectCallback<T> callback = this.callback;
            if (objectFormat.getCallBack() != null) {
                callback = click -> objectFormat.getCallBack().accept(click);
            }

            if (callback != null) {
                callback.accept(new PaginatedObjectEvent<>(this, builder, event, objectFormat.getObject()));
            }
        }
    }

    @Override
    public @NotNull PaginatedConfiguration getConfiguration() {
        return this.configuration;
    }

    @Override
    public int getSize() {
        return this.items.size();
    }

    @Override
    public int getObjectsSize() {
        return this.items.size();
    }

    @Override
    public int getCurrentPage() {
        return this.currentPage;
    }

    @Override
    public void setCurrentPage(@NotNull Inventory inventory, @NotNull InventoryBuilder<T> builder, int page) {
        Validate.isTrue(page > 0, "page must be positive");
        validate(builder, inventory);

        this.currentPage = page;

        this.clearInventory(inventory);
        this.format(inventory, builder);

        Button.update(inventory, builder, this);
    }

    @Override
    public int getPages() {
        return this.pages.size();
    }

    @Override
    public int getPagesSize() {
        return this.pages.size();
    }

    @Override
    public void format(@NotNull Inventory inventory, @NotNull InventoryBuilder<T> builder) {
        validate(builder, inventory);

        int size = configuration.getSize();
        int start = configuration.getStart();
        int exit = configuration.getEnd();

        calculate(size, this.items, this.pages);
        slots.clear();

        List<SimpleObjectFormatImpl<T>> values = this.pages.get(this.getCurrentPage());
        if (values == null) return;

        exit = exit <= 0 || exit >= inventory.getSize() ? inventory.getSize() - 1 : exit;

        SlotInvalidator validator = configuration.getValidator();
        for (int index = 0; index < values.size() && start < exit; start++) {
            SimpleObjectFormatImpl<T> format = values.get(index);

            int formatSlot = format.getSlot();
            if (formatSlot >= 0) {
                inventory.setItem(formatSlot, format.getItemStack(inventory, builder));
                slots.put(formatSlot, format);
                index++;

                continue;
            }

            T value = format.getObject();
            if (value instanceof InventorySlot) {
                InventorySlot inventorySlot = (InventorySlot) value;

                int itemSlot = inventorySlot.getSlot();
                if (itemSlot >= 0) {
                    inventory.setItem(itemSlot, format.getItemStack(inventory, builder));
                    slots.put(itemSlot, format);
                }

                index++;
                continue;
            }

            if (validator == null || !validator.validate(start)) {
                inventory.setItem(start, format.getItemStack(inventory, builder));
                slots.put(start, format);
                index++;
            }
        }

        updateButtons(inventory, builder);
    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory,
                       @Nullable ItemStack itemStack, int slot) {

        InventoryFormat<T> format = slots.get(slot);
        if (format instanceof ButtonFormat) return;

        if (format instanceof SimpleObjectFormatImpl) {
            SimpleObjectFormatImpl<T> objectFormat = (SimpleObjectFormatImpl<T>) format;
            objectFormat.setIcon(itemStack);
            inventory.setItem(slot, objectFormat.getItemStack(inventory, builder));
            return;
        }

        List<SimpleObjectFormatImpl<T>> formats = pages.get(this.getCurrentPage());
        if (formats != null) {
            SimpleObjectFormatImpl<T> objectFormat = new SimpleObjectFormatImpl<>(slot, itemStack, null, null);
            formats.add(objectFormat);

            inventory.setItem(slot, objectFormat.getItemStack(inventory, builder));
            slots.put(slot, objectFormat);
        }
    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory,
                       @Nullable ItemCallback<T> callback, @Nullable ItemStack itemStack,
                       int slot) {

        update(builder, inventory, itemStack, slot);

        InventoryFormat<T> format = slots.get(slot);
        if (format instanceof SimpleObjectFormatImpl) {
            SimpleObjectFormatImpl<T> objectFormat = (SimpleObjectFormatImpl<T>) format;
            objectFormat.setCallBack(click -> {
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

        InventoryFormat<T> format = slots.get(slot);
        if (format instanceof ButtonFormat) return;

        if (format instanceof SimpleObjectFormatImpl) {
            SimpleObjectFormatImpl<T> objectFormat = (SimpleObjectFormatImpl<T>) format;

            objectFormat.setObject(object);
            objectFormat.setIcon(null);

            inventory.setItem(slot, objectFormat.getItemStack(inventory, builder));
            return;
        }

        List<SimpleObjectFormatImpl<T>> formats = pages.get(this.getCurrentPage());
        if (formats != null) {
            SimpleObjectFormatImpl<T> objectFormat = new SimpleObjectFormatImpl<>(slot, object, null);
            formats.add(objectFormat);

            inventory.setItem(slot, objectFormat.getItemStack(inventory, builder));
            slots.put(slot, objectFormat);
        }
    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory,
                       @Nullable ObjectCallback<T> callback, @NotNull T object, int slot) {

        update(builder, inventory, object, slot);

        InventoryFormat<T> format = slots.get(slot);
        if (format instanceof SimpleObjectFormatImpl) {
            SimpleObjectFormatImpl<T> objectFormat = (SimpleObjectFormatImpl<T>) format;
            objectFormat.setCallBack(callback);
        }
    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory, @NotNull Collection<? extends T> objects) {
        validate(builder, inventory);
        clearInventory(inventory);

        this.pages.clear();
        this.items.clear();

        for (T item : objects) {
            this.items.add(new SimpleObjectFormatImpl<>(-1, item, null));
        }

        format(inventory, builder);
    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory,
                       @NotNull Collection<? extends T> objects, @Nullable PaginatedObjectCallback<T> callback) {

        update(builder, inventory, objects);

        this.callback = callback;
    }

    private void validate(InventoryBuilder<T> builder, Inventory inventory) {
        Validate.notNull(builder, "builder cannot be null");
        Validate.notNull(inventory, "inventory cannot be null");
    }

    private void validateConstructor(@NotNull PaginatedConfiguration configuration,
                                     @NotNull Object items,
                                     @Nullable PaginatedObjectCallback<T> callback) {

        Validate.notNull(configuration, "configuration cannot be null");
        Validate.notNull(items, "items cannot be null");

        this.configuration = configuration;
        this.callback = callback;
    }

    private void clearInventory(Inventory inventory) {
        for (int slot : this.slots.keySet()) {
            inventory.setItem(slot, null);
        }
    }

    private void updateButtons(@NotNull Inventory inventory, @NotNull InventoryBuilder<T> builder) {
        for (ButtonFormat<T> button : buttons) {
            if (button.update(inventory, builder, this)) {
                slots.put(button.getSlot(), button);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaginatedObjectFormatImpl)) return false;

        PaginatedObjectFormatImpl<?> that = (PaginatedObjectFormatImpl<?>) o;
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
