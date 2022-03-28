package br.com.blecaute.inventory.format.updater;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.callback.ItemCallback;
import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ItemUpdater<T extends InventoryItem> {

    default void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory, int slot,
                        @Nullable ItemStack itemStack, @Nullable ItemCallback<T> callback) {}

    default void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory, int slot,
                        @NotNull List<ItemStack> items, @Nullable ItemCallback<T> callback) {}

}
