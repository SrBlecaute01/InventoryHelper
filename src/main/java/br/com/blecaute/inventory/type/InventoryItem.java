package br.com.blecaute.inventory.type;

import br.com.blecaute.inventory.property.InventoryProperty;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Any class that implements this can be used in InventoryBuilder
 * to represent object as an @{@link ItemStack} in @{@link Inventory}
 */
public interface InventoryItem {

    @Nullable ItemStack getItem(@NotNull Inventory inventory, @NotNull InventoryProperty property);

}