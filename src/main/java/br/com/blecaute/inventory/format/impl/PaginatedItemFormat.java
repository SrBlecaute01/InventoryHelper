package br.com.blecaute.inventory.format.impl;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.callback.ItemCallback;
import br.com.blecaute.inventory.event.InventoryEvent;
import br.com.blecaute.inventory.format.PaginatedFormat;
import br.com.blecaute.inventory.type.InventoryItem;
import br.com.blecaute.inventory.util.ListUtil;
import lombok.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@Data
public class PaginatedItemFormat<T extends InventoryItem> implements PaginatedFormat<T> {

    @NonNull
    private final List<ItemStack> items;
    private final ItemCallback<T> callBack;

    private final Set<Integer> slots = new HashSet<>();

    @Override
    public boolean isValid(int slot) {
        return slots.contains(slot);
    }

    @Override
    public void accept(@NotNull InventoryClickEvent event, @NotNull InventoryBuilder<T> builder) {
        accept(callBack, new InventoryEvent<>(event, event.getCurrentItem(), builder.getProperties(), null));
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

        List<ItemStack> values = size <= 0 ? items : ListUtil.getSublist(items, page, size);
        for(int index = 0; index < values.size() && slot < exit; slot++) {

            if(skipFunction != null && skipFunction.apply(slot)) continue;

            ItemStack item = values.get(index);
            inventory.setItem(slot, item);

            slots.add(slot);
            index++;
        }
    }
}
