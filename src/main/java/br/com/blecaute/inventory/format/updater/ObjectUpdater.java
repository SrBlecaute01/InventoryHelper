package br.com.blecaute.inventory.format.updater;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.callback.ObjectCallback;
import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ObjectUpdater<T extends InventoryItem> {

    default void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory) {}

    default void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory, int slot,
                @NotNull T object, @Nullable ObjectCallback<T> callback) {}

    default void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory, int slot,
                @NotNull List<T> objects, @Nullable ObjectCallback<T> callback) {}

}
