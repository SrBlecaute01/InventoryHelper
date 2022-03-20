package br.com.blecaute.inventory.format;

import br.com.blecaute.inventory.callback.InventoryCallback;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface UpdatableFormat<T extends InventoryCallback<?,?>> {

    void update(int slot, @Nullable ItemStack itemStack, @Nullable T callback);

    void flush(@NotNull Inventory inventory);

}