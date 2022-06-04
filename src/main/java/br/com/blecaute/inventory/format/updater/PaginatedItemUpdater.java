package br.com.blecaute.inventory.format.updater;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.callback.PaginatedItemCallback;
import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface PaginatedItemUpdater<T extends InventoryItem> extends ItemUpdater<T> {

    void update(@NotNull InventoryBuilder<T> builder,
                @NotNull Inventory inventory,
                @NotNull Collection<ItemStack> items);

    void update(@NotNull InventoryBuilder<T> builder,
                @NotNull Inventory inventory,
                @NotNull Collection<ItemStack> items,
                @Nullable PaginatedItemCallback<T> callback);

}