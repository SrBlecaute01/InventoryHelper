package br.com.blecaute.inventory.event;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.callback.PaginatedItemCallback;
import br.com.blecaute.inventory.event.updatable.UpdatableItemPaginated;
import br.com.blecaute.inventory.format.InventoryFormat;
import br.com.blecaute.inventory.format.PaginatedFormat;
import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class PaginatedItemEvent<T extends InventoryItem> extends ItemClickEvent<T> implements UpdatableItemPaginated<T> {

    public PaginatedItemEvent(@NotNull InventoryFormat<T> format,
                              @NotNull InventoryBuilder<T> builder,
                              @NotNull InventoryClickEvent event) {

        super(format, builder, event);
    }

    @Override
    public void update(@NotNull Collection<ItemStack> items) {
        updater.updateItem(getIdentifier(), items);
    }

    @Override
    public void update(@NotNull Collection<ItemStack> items, @NotNull PaginatedItemCallback<T> callback) {
        updater.updateItems(getIdentifier(), items, callback);
    }

    private @NotNull String getIdentifier() {
        return ((PaginatedFormat<T>) format).getConfiguration().getIdentifier();
    }

}
