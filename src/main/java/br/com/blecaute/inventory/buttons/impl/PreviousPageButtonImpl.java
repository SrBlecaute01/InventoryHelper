package br.com.blecaute.inventory.buttons.impl;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.buttons.Button;
import br.com.blecaute.inventory.format.InventoryFormat;
import br.com.blecaute.inventory.property.InventoryProperty;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PreviousPageButtonImpl implements Button {
    
    @Override
    public void accept(@NotNull InventoryBuilder<?> builder, @NotNull InventoryFormat<?> format) {
        
    }

    @Override
    public @Nullable ItemStack getItem(@NotNull Inventory inventory, @NotNull InventoryProperty property) {
        return null;
    }

    @Override
    public int getSlot() {
        return 0;
    }
}
