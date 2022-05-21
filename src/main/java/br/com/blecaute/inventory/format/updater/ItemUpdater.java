package br.com.blecaute.inventory.format.updater;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.callback.ItemCallback;
import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ItemUpdater<T extends InventoryItem> {

    void update(@NotNull InventoryBuilder<T> builder,
                @NotNull Inventory inventory,
                @Nullable ItemStack itemStack);

    void update(@NotNull InventoryBuilder<T> builder,
                @NotNull Inventory inventory,
                @Nullable ItemCallback<T> callback,
                @Nullable ItemStack itemStack);

}
