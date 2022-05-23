package br.com.blecaute.inventory.format.updater;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.callback.ObjectCallback;
import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ObjectUpdater<T extends InventoryItem> extends ItemUpdater<T> {

    void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory,
                @NotNull T object);

    void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory,
                @Nullable ObjectCallback<T> callback, @NotNull T object);

}
