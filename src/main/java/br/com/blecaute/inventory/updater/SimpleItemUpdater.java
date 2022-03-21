package br.com.blecaute.inventory.updater;

import br.com.blecaute.inventory.callback.ItemCallback;
import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface SimpleItemUpdater<T extends InventoryItem> extends InventoryUpdater {

    void update(@Nullable ItemStack itemStack);

    void update(@Nullable ItemStack itemStack, @Nullable ItemCallback<T> callback);

    void update(int slot, @Nullable ItemStack itemStack, @Nullable ItemCallback<T> callback);

}
