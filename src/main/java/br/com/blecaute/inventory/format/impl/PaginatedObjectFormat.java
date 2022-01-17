package br.com.blecaute.inventory.format.impl;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.event.InventoryClick;
import br.com.blecaute.inventory.format.PaginatedFormat;
import br.com.blecaute.inventory.type.InventoryItem;
import br.com.blecaute.inventory.util.ListUtil;
import lombok.Data;
import lombok.NonNull;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

@Data
public class PaginatedObjectFormat<T extends InventoryItem> implements PaginatedFormat<T> {

    @NonNull
    private final List<T> items;
    private final Consumer<InventoryClick<T>> consumer;

    private final Map<Integer, T> slots = new HashMap<>();

    @Override
    public boolean isValid(int slot) {
        return slots.containsKey(slot);
    }

    @Override
    public void accept(@NotNull InventoryClickEvent event, @NotNull InventoryBuilder<T> builder) {
        accept(consumer, new InventoryClick<>(event, event.getCurrentItem(), slots.get(event.getRawSlot())));
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

        List<T> values = size <= 0 ? items : ListUtil.getSublist(items, page, size);
        for(int index = 0; index < values.size() && slot <= exit; slot++) {

            if(skipFunction != null && skipFunction.apply(slot)) continue;

            T value = values.get(index);
            inventory.setItem(slot, value.getItem(inventory, builder.getProperties()));

            slots.put(slot, value);
            index++;
        }

    }
}
