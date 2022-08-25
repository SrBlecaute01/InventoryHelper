package br.com.blecaute.inventory.button.impl;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.button.Button;
import br.com.blecaute.inventory.format.InventoryFormat;
import br.com.blecaute.inventory.format.PaginatedFormat;
import br.com.blecaute.inventory.property.InventoryProperty;
import br.com.blecaute.inventory.type.InventoryItem;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public class NextPageButtonImpl implements Button {
    
    private final int slot;
    private final ItemStack itemStack;
    private final boolean alwaysShow;

    @Override
    public <T extends InventoryItem> void accept(@NotNull InventoryClickEvent event, @NotNull InventoryBuilder<T> builder,
                                                 @NotNull InventoryFormat<T> format) {

        if (format instanceof PaginatedFormat) {
            PaginatedFormat<T> paginated = (PaginatedFormat<T>) format;

            int current = paginated.getCurrentPage();
            if (current < paginated.getPages()) {
                Inventory inventory = event.getInventory();
                paginated.setCurrentPage(inventory, builder, current + 1);
            }
        }
    }

    @Override
    public <T extends InventoryItem> boolean canPlace(@NotNull InventoryBuilder<T> builder, @NotNull InventoryFormat<T> format) {
        if (alwaysShow) return true;

        if (format instanceof PaginatedFormat) {
            PaginatedFormat<T> paginated = (PaginatedFormat<T>) format;

            int current = paginated.getCurrentPage();
            return current < paginated.getPages();
        }

        return false;
    }

    @Override
    public @Nullable ItemStack getItem(@NotNull Inventory inventory, @NotNull InventoryProperty property) {
        return itemStack;
    }

    @Override
    public int getSlot() {
        return slot;
    }

}
