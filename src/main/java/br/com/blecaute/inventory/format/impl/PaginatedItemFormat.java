package br.com.blecaute.inventory.format.impl;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.callback.ItemCallback;
import br.com.blecaute.inventory.format.PaginatedFormat;
import br.com.blecaute.inventory.format.UpdatableFormat;
import br.com.blecaute.inventory.type.InventoryItem;
import br.com.blecaute.inventory.util.ListUtil;
import lombok.Getter;
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

@Getter
public class PaginatedItemFormat<T extends InventoryItem> implements PaginatedFormat<T>, UpdatableFormat<ItemCallback<T>> {

    private final ItemCallback<T> callback;

    private final List<SimpleItemFormat<T>> items = new ArrayList<>();
    private final Map<Integer, SimpleItemFormat<T>> slots = new HashMap<>();

    public PaginatedItemFormat(@NonNull List<ItemStack> items, @Nullable ItemCallback<T> callBack) {
        this.callback = callBack;
        this.items.addAll(items.stream()
                .map(item -> new SimpleItemFormat<>(-1, item, callBack))
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
    public void format(@NotNull Inventory inventory,
                       @NotNull InventoryBuilder<T> builder,
                       @Nullable Function<Integer, Boolean> skipFunction) {

        slots.clear();

        int slot = builder.getStartSlot();
        int exit = builder.getExitSlot();
        int size = builder.getPageSize();
        int page = builder.getCurrentPage();

        List<SimpleItemFormat<T>> values = size <= 0 ? items : ListUtil.getSublist(items, page, size);
        for(int index = 0; index < values.size() && slot < exit; slot++) {
            SimpleItemFormat<T> format = values.get(index);
            if (format.getSlot() >= 0) {
                inventory.setItem(slot, format.getItemStack());
                continue;
            }

            if(skipFunction != null && skipFunction.apply(slot)) continue;

            ItemStack item = format.getItemStack();
            inventory.setItem(slot, item);

            slots.put(slot, format);
            index++;
        }
    }

    public void update(int slot, ItemStack itemStack, ItemCallback<T> callback) {
        SimpleItemFormat<T> format = slots.get(slot);
        if (format != null) {
            format.update(slot, itemStack, callback);
        }
    }

    @Override
    public void flush(@NotNull Inventory inventory) {
        slots.values().forEach(format -> format.flush(inventory));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaginatedItemFormat)) return false;

        PaginatedItemFormat<?> that = (PaginatedItemFormat<?>) o;
        return getItems().equals(that.getItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getItems());
    }
}
