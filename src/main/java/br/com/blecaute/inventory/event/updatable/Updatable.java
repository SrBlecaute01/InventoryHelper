package br.com.blecaute.inventory.event.updatable;

import br.com.blecaute.inventory.callback.ItemCallback;
import br.com.blecaute.inventory.callback.ObjectCallback;
import br.com.blecaute.inventory.callback.PaginatedItemCallback;
import br.com.blecaute.inventory.callback.PaginatedObjectCallback;
import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * The class that implements the Updatable indicates that it
 * has methods to update the item in the inventory.
 */
public interface Updatable<T extends InventoryItem> {

    void update();

    void updateItem(int slot, @Nullable ItemStack item);

    void updateItem(int slot, @Nullable ItemStack item, @Nullable ItemCallback<T> callback);

    void updateItem(@NotNull String identifier, @NotNull Collection<ItemStack> items);

    void updateItems(@NotNull String identifier, @NotNull Collection<ItemStack> items, @Nullable PaginatedItemCallback<T> callback);

    void updateObject(int slot, @NotNull T object);

    void updateObject(int slot, @NotNull T object, @Nullable ObjectCallback<T> callback);

    void updateObjects(@NotNull String identifier, @NotNull Collection<T> objects);

    void updateObjects(@NotNull String identifier, @NotNull Collection<T> objects, @Nullable PaginatedObjectCallback<T> callback);

}