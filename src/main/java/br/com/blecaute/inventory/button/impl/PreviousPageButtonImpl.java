package br.com.blecaute.inventory.button.impl;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.format.PaginatedFormat;
import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PreviousPageButtonImpl extends AbstractPageButtonImpl {

    public PreviousPageButtonImpl(int slot, ItemStack itemStack, boolean alwaysShow) {
        super(slot, itemStack, alwaysShow);
    }

    <T extends InventoryItem> boolean canChange(@NotNull PaginatedFormat<T> format) {
        return format.getCurrentPage() > 1;
    }

    <T extends InventoryItem> void change(@NotNull InventoryEvent event,
                                          @NotNull InventoryBuilder<T> builder,
                                          @NotNull PaginatedFormat<T> format) {

        int current = format.getCurrentPage();
        if (!alwaysShow || current > 1) {
            format.setCurrentPage(event.getInventory(), builder, current - 1);
        }
    }

}
