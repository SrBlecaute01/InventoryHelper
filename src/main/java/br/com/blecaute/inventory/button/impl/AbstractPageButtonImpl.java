package br.com.blecaute.inventory.button.impl;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.button.Button;
import br.com.blecaute.inventory.format.ButtonFormat;
import br.com.blecaute.inventory.format.InventoryFormat;
import br.com.blecaute.inventory.format.PaginatedFormat;
import br.com.blecaute.inventory.property.InventoryProperty;
import br.com.blecaute.inventory.type.InventoryItem;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public abstract class AbstractPageButtonImpl implements Button {

    protected final int slot;
    protected final ItemStack itemStack;
    protected final boolean alwaysShow;

    @Override
    public <T extends InventoryItem> void accept(@NotNull InventoryEvent event, @NotNull InventoryBuilder<T> builder,
                                                 @NotNull InventoryFormat<T> format) {

        Validate.notNull(event, "event cannot be null");
        validate(builder, format);

        if (format instanceof PaginatedFormat) {
            change(event, builder, (PaginatedFormat<T>) format);
            return;
        }

        if (format instanceof ButtonFormat) {
            for (InventoryFormat<T> inventoryFormat : builder.getFormats()) {
                if (inventoryFormat instanceof PaginatedFormat) {
                    change(event, builder, (PaginatedFormat<T>) inventoryFormat);
                }
            }
        }
    }

    @Override
    public <T extends InventoryItem> boolean canFormat(@NotNull Inventory inventory, @NotNull InventoryBuilder<T> builder,
                                                       @NotNull InventoryFormat<T> format) {

        Validate.notNull(inventory, "inventory cannot be null");
        validate(builder, format);

        if (alwaysShow) return true;
        if (format instanceof PaginatedFormat) {
            return canChange((PaginatedFormat<T>) format);
        }

        if (format instanceof ButtonFormat) {
            for (InventoryFormat<T> inventoryFormat : builder.getFormats()) {
                if (inventoryFormat instanceof PaginatedFormat) {
                    PaginatedFormat<T> paginatedFormat = (PaginatedFormat<T>) inventoryFormat;
                    if (canChange(paginatedFormat)) return true;
                }
            }
        }

        return false;
    }

    abstract <T extends InventoryItem> boolean canChange(@NotNull PaginatedFormat<T> format);

    abstract <T extends InventoryItem> void change(@NotNull InventoryEvent event, @NotNull InventoryBuilder<T> builder,
                                                   @NotNull PaginatedFormat<T> format);

    @Override
    public @Nullable ItemStack getItem(@NotNull Inventory inventory, @NotNull InventoryProperty property) {
        return itemStack;
    }

    @Override
    public int getSlot() {
        return slot;
    }

    private void validate(InventoryBuilder<?> builder,  InventoryFormat<?> format) {
        Validate.notNull(builder, "builder cannot be null");
        Validate.notNull(format, "format cannot be null");
    }

}
