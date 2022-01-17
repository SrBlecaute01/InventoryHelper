package br.com.blecaute.inventory.type;

import br.com.blecaute.inventory.property.InventoryProperty;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface InventoryItemType {

    @Nullable ItemStack getItem(@NotNull Inventory inventory, @NotNull InventoryProperty property);

}