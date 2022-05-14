package br.com.blecaute.inventory.event.updatable;

import br.com.blecaute.inventory.callback.ItemCallback;
import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface UpdatableItem<T extends InventoryItem> extends Updatable {

    void update(@NotNull ItemStack itemStack);

    void update(@NotNull ItemStack itemStack, @Nullable ItemCallback<T> callback);

    void update(int slot, @NotNull ItemStack itemStack, @Nullable ItemCallback<T> callback);

}