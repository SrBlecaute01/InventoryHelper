package br.com.blecaute.inventory.format.impl;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.callback.ItemCallback;
import br.com.blecaute.inventory.callback.ObjectCallback;
import br.com.blecaute.inventory.event.ItemClickEvent;
import br.com.blecaute.inventory.format.PaginatedFormat;
import br.com.blecaute.inventory.format.updater.ItemUpdater;
import br.com.blecaute.inventory.format.updater.ObjectUpdater;
import br.com.blecaute.inventory.type.InventoryItem;
import br.com.blecaute.inventory.type.InventorySlot;
import lombok.NonNull;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PaginatedObjectFormat<T extends InventoryItem> implements PaginatedFormat<T>, ItemUpdater<T>, ObjectUpdater<T> {

    private ObjectCallback<T> callback;

    private final Function<Integer, Boolean> function;

    private final List<SimpleObjectFormat<T>> items = new ArrayList<>();
    private final Map<Integer, SimpleObjectFormat<T>> slots = new HashMap<>();
    private final Map<Integer, List<SimpleObjectFormat<T>>> pages = new HashMap<>();

    public PaginatedObjectFormat(@NonNull List<T> items, Function<Integer, Boolean> function,
                                 @Nullable ObjectCallback<T> callback) {

        this.callback = callback;
        this.function = function;
        this.items.addAll(items.stream()
                .map(value -> new SimpleObjectFormat<>(-1, value, callback))
                .collect(Collectors.toList()));
    }

    @Override
    public boolean isValid(int slot) {
        return slots.containsKey(slot);
    }

    @Override
    public void accept(@NotNull InventoryClickEvent event, @NotNull InventoryBuilder<T> builder) {
        SimpleObjectFormat<T> format = slots.get(event.getRawSlot());
        if (format != null) {
            format.accept(event, builder);
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
        slots.clear();

        calculate(builder.getPageSize(), this.items, this.pages);

        int slot = builder.getStartSlot();
        int exit = builder.getExitSlot();
        int page = builder.getCurrentPage();

        List<SimpleObjectFormat<T>> values = this.pages.getOrDefault(page - 1, Collections.emptyList());
        for(int index = 0; index < values.size() && slot < exit; slot++) {

            SimpleObjectFormat<T> format = values.get(index);
            if (format.getSlot() >= 0) {
                inventory.setItem(slot, format.getItemStack(inventory, builder));
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

            if(function != null && function.apply(slot)) continue;

            inventory.setItem(slot, format.getItemStack(inventory, builder));
            slots.put(slot, format);

            index++;
        }

    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory) {
        format(inventory, builder);
    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory, int slot,
                       @Nullable ItemStack itemStack, @Nullable ItemCallback<T> callback) {

        SimpleObjectFormat<T> format = slots.get(slot);
        if (format != null) {
            format.update(builder, inventory, -1, itemStack, callback);
            return;
        }

        format = new SimpleObjectFormat<>(slot, itemStack, null, callback == null ? this.callback : callback::accept);
        format.format(inventory, builder);

        List<SimpleObjectFormat<T>> formats = pages.get(builder.getCurrentPage());
        if (formats != null) {
            formats.add(format);
        }

        slots.put(slot, format);
    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory, int slot,
                       @NotNull List<ItemStack> items, @Nullable ItemCallback<T> callback) {

        this.callback = callback == null ? this.callback : callback::accept;
        this.pages.put(builder.getCurrentPage(), items.stream()
                .map(item -> new SimpleObjectFormat<>(-1, item,null, this.callback))
                .collect(Collectors.toList()));

        format(inventory, builder);
    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory, int slot,
                       @NotNull List<T> objects, @Nullable ObjectCallback<T> callback) {

        this.callback = callback == null ? this.callback : callback;
        this.pages.put(builder.getCurrentPage(), objects.stream()
                .map(item -> new SimpleObjectFormat<>(-1, item, this.callback))
                .collect(Collectors.toList()));

        format(inventory, builder);
    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory, int slot,
                       @NotNull T object, @Nullable ObjectCallback<T> callback) {

        SimpleObjectFormat<T> format = slots.get(slot);
        if (format != null) {
            format.update(builder, inventory, -1, object, callback);
            return;
        }

        format = new SimpleObjectFormat<>(slot, object, callback == null ? this.callback : callback);
        format.format(inventory, builder);

        List<SimpleObjectFormat<T>> formats = pages.get(builder.getCurrentPage());
        if (formats != null) {
            formats.add(format);
        }

        slots.put(slot, format);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaginatedObjectFormat)) return false;

        PaginatedObjectFormat<?> that = (PaginatedObjectFormat<?>) o;
        return this.items.equals(that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.items);
    }
}
