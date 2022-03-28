package br.com.blecaute.inventory.event;

import br.com.blecaute.inventory.callback.ItemCallback;
import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface UpdatableItem<T extends InventoryItem> extends Updatable {

    void update(@Nullable ItemStack itemStack);

    void update(@Nullable ItemStack itemStack, @Nullable ItemCallback<T> callback);

    void update(int slot, @Nullable ItemStack itemStack, @Nullable ItemCallback<T> callback);

}