package br.com.blecaute.inventory.format.impl;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.callback.ItemCallback;
import br.com.blecaute.inventory.format.PaginatedFormat;
import br.com.blecaute.inventory.format.updater.ItemUpdater;
import br.com.blecaute.inventory.type.InventoryItem;
import lombok.NonNull;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PaginatedItemFormat<T extends InventoryItem> implements PaginatedFormat<T>, ItemUpdater<T> {

    private ItemCallback<T> callback;

    private final Function<Integer, Boolean> function;

    private final List<SimpleItemFormat<T>> items = new ArrayList<>();
    private final Map<Integer, SimpleItemFormat<T>> slots = new HashMap<>();
    private final Map<Integer, List<SimpleItemFormat<T>>> pages = new HashMap<>();

    public PaginatedItemFormat(@NonNull List<ItemStack> items,
                               @Nullable Function<Integer, Boolean> function,
                               @Nullable ItemCallback<T> callBack) {

        this.function = function;
        this.callback = callBack;
        this.items.addAll(items.stream()
                .map(value -> new SimpleItemFormat<>(-1, value, callback))
                .collect(Collectors.toList()));
    }

    @Override
    public boolean isValid(int slot) {
        return slots.containsKey(slot);
    }

    @Override
    public void accept(@NotNull InventoryClickEvent event, @NotNull InventoryBuilder<T> builder) {
        SimpleItemFormat<T> format = slots.get(event.getRawSlot());
        if (format != null && format.getCallBack() != null) {
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

        List<SimpleItemFormat<T>> values = this.pages.getOrDefault(page - 1, Collections.emptyList());
        for(int index = 0; index < values.size() && slot < exit; slot++) {
            SimpleItemFormat<T> format = values.get(index);
            if (format.getSlot() >= 0) {
                inventory.setItem(slot, format.getItemStack());
                continue;
            }

            if(function != null && function.apply(slot)) continue;

            ItemStack item = format.getItemStack();
            inventory.setItem(slot, item);

            slots.put(slot, format);
            index++;
        }
    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory, int slot,
                       @NotNull List<ItemStack> items, @Nullable ItemCallback<T> callback) {

        this.callback = callback == null ? this.callback : callback;
        this.pages.put(builder.getCurrentPage(), items.stream()
                .map(item -> new SimpleItemFormat<>(-1, item, this.callback))
                .collect(Collectors.toList()));

        format(inventory, builder);
    }

    @Override
    public void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory, int slot,
                       @Nullable ItemStack itemStack, @Nullable ItemCallback<T> callback) {

        SimpleItemFormat<T> format = slots.get(slot);
        if (format != null) {
            format.update(builder, inventory, -1, itemStack, callback);
            return;
        }

        format = new SimpleItemFormat<>(slot, itemStack, callback == null ? this.callback : callback);
        format.format(inventory, builder);

        List<SimpleItemFormat<T>> formats = pages.get(builder.getCurrentPage());
        if (formats != null) {
            formats.add(format);
        }

        slots.put(slot, format);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaginatedItemFormat)) return false;

        PaginatedItemFormat<?> that = (PaginatedItemFormat<?>) o;
        return this.items.equals(that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.items);
    }
}
