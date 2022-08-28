package br.com.blecaute.inventory.format.impl;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.button.Button;
import br.com.blecaute.inventory.callback.ItemCallback;
import br.com.blecaute.inventory.callback.PaginatedItemCallback;
import br.com.blecaute.inventory.configuration.PaginatedConfiguration;
import br.com.blecaute.inventory.event.PaginatedItemEvent;
import br.com.blecaute.inventory.format.ButtonFormat;
import br.com.blecaute.inventory.format.InventoryFormat;
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

public class PaginatedItemFormatImpl<T extends InventoryItem> implements PaginatedFormat<T>, PaginatedItemUpdater<T> {

    private PaginatedConfiguration configuration;
    private PaginatedItemCallback<T> callback;

    private final List<SimpleItemFormatImpl<T>> items = new ArrayList<>();
    private final List<ButtonFormatImpl<T>> buttons = new ArrayList<>();

    private final Map<Integer, InventoryFormat<T>> slots = new HashMap<>();
    private final Map<Integer, List<SimpleItemFormatImpl<T>>> pages = new HashMap<>();

    private int currentPage = 1;

    public PaginatedItemFormatImpl(@NotNull PaginatedConfiguration configuration,
                                   @NotNull Collection<ItemStack> items,
                                   @Nullable PaginatedItemCallback<T> callback) {

        validateConstructor(configuration, items, callback);

        for (ItemStack item : items) {
            this.items.add(new SimpleItemFormatImpl<>(-1, item, null));
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

        InventoryFormat<T> format = this.slots.get(event.getRawSlot());
        if (format == null) return;

        if (format instanceof ButtonFormat) {
            ButtonFormat<T> button = (ButtonFormat<T>) format;
            button.accept(event, builder, this);
            return;
        }

        if (format instanceof SimpleItemFormatImpl) {
            SimpleItemFormatImpl<T> item = (SimpleItemFormatImpl<T>) format;
            PaginatedItemCallback<T> callback = this.callback;

            if (item.getCallBack() != null) {
                callback = click -> item.getCallBack().accept(click);
            }

            if (callback != null) {
                callback.accept(new PaginatedItemEvent<>(this, builder, event));
            }
        }
    }

    @Override
    public @NotNull PaginatedConfiguration getConfiguration() {
        return this.configuration;
    }

    @Override
    public int getSize() {
        return items.size();
    }

    @Override
    public int getObjectsSize() {
        return this.items.size();
    }

    @Override
    public int getPages() {
        return pages.size();
    }

    @Override
    public int getPagesSize() {
        return this.pages.size();
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
    public void format(@NotNull Inventory inventory, @NotNull InventoryBuilder<T> builder) {
        validate(builder, inventory);

        int start = configuration.getStart();
        int exit = configuration.getEnd();
        int size = configuration.getSize();
       
        calculate(size, this.items, this.pages);
        slots.clear();

        if (exit <= 0 || exit >= inventory.getSize()) {
            exit = inventory.getSize() - 1;
        }

        List<SimpleItemFormatImpl<T>> values = this.pages.get(this.getCurrentPage());
        if (values == null) return;

        SlotInvalidator validator = configuration.getValidator();
        for (int index = 0; index < values.size() && start < exit; start++) {
            SimpleItemFormatImpl<T> format = values.get(index);
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

        for (ButtonFormat<T> button : buttons) {
            if (button.update(inventory, builder, this)) {
                slots.put(button.getSlot(), button);
            }
        }
    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory,
                       @Nullable ItemStack itemStack, int slot) {

        validate(builder, inventory);

        InventoryFormat<T> format = slots.get(slot);
        if (format instanceof ButtonFormat) return;

        if (format instanceof SimpleItemFormatImpl) {
            SimpleItemFormatImpl<T> itemFormat = (SimpleItemFormatImpl<T>) format;

            itemFormat.setItemStack(itemStack);
            inventory.setItem(slot, itemFormat.getItemStack(inventory, builder));
            return;
        }

        List<SimpleItemFormatImpl<T>> formats = pages.get(this.getCurrentPage());
        if (formats != null) {
            SimpleItemFormatImpl<T> itemFormat = new SimpleItemFormatImpl<>(slot, itemStack, null);
            formats.add(itemFormat);

            inventory.setItem(slot, itemFormat.getItemStack(inventory, builder));
            slots.put(slot, format);
        }
    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory,
                       @Nullable ItemCallback<T> callback, @Nullable ItemStack itemStack,
                       int slot) {

        update(builder, inventory, itemStack, slot);

        InventoryFormat<T> format = slots.get(slot);
        if (format instanceof SimpleItemFormatImpl) {
            SimpleItemFormatImpl<T> itemFormat = (SimpleItemFormatImpl<T>) format;
            itemFormat.setCallBack(callback);
        }
    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory, @NotNull Collection<ItemStack> items) {
        validate(builder, inventory);
        clearInventory(inventory);

        this.pages.clear();
        this.items.clear();

        for (ItemStack item : items) {
            this.items.add(new SimpleItemFormatImpl<>(-1, item, null));
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

    private void validateConstructor(PaginatedConfiguration configuration, Object items, PaginatedItemCallback<T> callBack) {
        Validate.notNull(configuration, "configuration cannot be null");
        Validate.notNull(items, "items cannot be null");

        this.configuration = configuration;
        this.callback = callBack;
    }

    private void clearInventory(@NotNull Inventory inventory) {
        for (int slot : this.slots.keySet()) {
            inventory.setItem(slot, null);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaginatedItemFormatImpl)) return false;

        PaginatedItemFormatImpl<?> that = (PaginatedItemFormatImpl<?>) o;
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
