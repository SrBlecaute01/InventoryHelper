package br.com.blecaute.inventory.format.impl;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.callback.ItemCallback;
import br.com.blecaute.inventory.callback.PaginatedItemCallback;
import br.com.blecaute.inventory.event.PaginatedItemEvent;
import br.com.blecaute.inventory.format.PaginatedFormat;
import br.com.blecaute.inventory.format.updater.PaginatedItemUpdater;
import br.com.blecaute.inventory.type.InventoryItem;
import br.com.blecaute.inventory.validator.SlotValidator;
import lombok.NonNull;
import org.apache.commons.lang.Validate;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PaginatedItemFormat<T extends InventoryItem> implements PaginatedFormat<T>, PaginatedItemUpdater<T> {

    private final PaginatedItemCallback<T> callback;
    private final SlotValidator validator;

    private final List<SimpleItemFormat<T>> items = new ArrayList<>();
    private final Map<Integer, SimpleItemFormat<T>> slots = new HashMap<>();
    private final Map<Integer, List<SimpleItemFormat<T>>> pages = new HashMap<>();

    public PaginatedItemFormat(@NonNull List<ItemStack> items,
                               @Nullable SlotValidator validator,
                               @Nullable PaginatedItemCallback<T> callBack) {

        Validate.notNull(items, "items cannot be null");

        this.validator = validator;
        this.callback = callBack;

        for (ItemStack item : items) {
            this.items.add(new SimpleItemFormat<>(-1, item, null));
        }

    }

    @Override
    public boolean isValid(int slot) {
        return slots.containsKey(slot);
    }

    @Override
    public void accept(@NotNull InventoryClickEvent event, @NotNull InventoryBuilder<T> builder) {
        Validate.notNull(event, "event cannot be null");
        Validate.notNull(builder, "builder cannot be null");

        SimpleItemFormat<T> format = slots.get(event.getRawSlot());
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
    public int getSize() {
        return items.size();
    }

    @Override
    public int getPages() {
        return pages.size();
    }

    @Override
    public void format(@NotNull Inventory inventory, @NotNull InventoryBuilder<T> builder) {
        Validate.notNull(inventory, "inventory cannot be null");
        Validate.notNull(builder, "builder cannot be null");

        calculate(builder.getPageSize(), this.items, this.pages);

        slots.clear();

        int slot = builder.getStartSlot();
        int exit = builder.getExitSlot();
        int page = builder.getCurrentPage();

        List<SimpleItemFormat<T>> values = this.pages.getOrDefault(page, Collections.emptyList());
        for(int index = 0; index < values.size() && slot < exit; slot++) {
            SimpleItemFormat<T> format = values.get(index);
            if (format.getSlot() >= 0) {
                inventory.setItem(slot, format.getItemStack());
                index++;
                continue;
            }

            if(validator != null && validator.validate(slot)) continue;

            ItemStack item = format.getItemStack();
            inventory.setItem(slot, item);

            slots.put(slot, format);
            index++;
        }
    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory,
                       @NotNull ItemStack itemStack, int slot) {

        Validate.notNull(builder, "builder cannot be null");
        Validate.notNull(inventory, "inventory cannot be null");
        Validate.notNull(itemStack, "itemStack cannot be null");

        SimpleItemFormat<T> format = slots.get(slot);
        if (format != null) {
            format.setItemStack(itemStack);
            inventory.setItem(slot, format.getItemStack(inventory, builder));
        }

        List<SimpleItemFormat<T>> formats = pages.get(builder.getCurrentPage());
        if (formats != null) {
            format = new SimpleItemFormat<>(slot, itemStack, null);
            formats.add(format);

            inventory.setItem(slot, format.getItemStack(inventory, builder));
            slots.put(slot, format);
        }
    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory,
                       @Nullable ItemCallback<T> callback, @NotNull ItemStack itemStack, int slot) {

        update(builder, inventory, itemStack, slot);

        SimpleItemFormat<T> format = slots.get(slot);
        if (format != null) {
            format.setCallBack(callback);
        }
    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory, @NotNull Collection<ItemStack> items) {
        this.items.clear();
        this.pages.clear();

        for (ItemStack item : items) {
            this.items.add(new SimpleItemFormat<>(-1, item, null));
        }

        format(inventory, builder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.items);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaginatedItemFormat)) return false;

        PaginatedItemFormat<?> that = (PaginatedItemFormat<?>) o;
        return this.items.equals(that.items);
    }

}
