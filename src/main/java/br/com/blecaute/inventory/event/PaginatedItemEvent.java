package br.com.blecaute.inventory.event;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.event.updatable.UpdatableItemPaginated;
import br.com.blecaute.inventory.format.InventoryFormat;
import br.com.blecaute.inventory.format.updater.PaginatedItemUpdater;
import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class PaginatedItemEvent<T extends InventoryItem> extends ItemClickEvent<T> implements UpdatableItemPaginated<T> {

    public PaginatedItemEvent(@NotNull InventoryFormat<T> format,
                              @NotNull InventoryBuilder<T> builder,
                              @NotNull InventoryClickEvent event) {

        super(format, builder, event);
    }

    @Override
    public void update(@NotNull Collection<ItemStack> items) {
        if (!(format instanceof PaginatedItemUpdater)) {
            throw new UnsupportedOperationException("The format is not an instance of PaginatedObjectFormat");
        }

        ((PaginatedItemUpdater<T>) format).update(getBuilder(), getInventory(), items);
    }

}
