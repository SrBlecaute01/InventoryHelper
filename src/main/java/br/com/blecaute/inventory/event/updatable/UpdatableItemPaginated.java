package br.com.blecaute.inventory.event.updatable;

import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface UpdatableItemPaginated<T extends InventoryItem> extends UpdatableItem<T> {

    void update(@NotNull Collection<ItemStack> items);

}