package br.com.blecaute.inventory.format.updater;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.callback.ObjectCallback;
import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public interface ObjectUpdater<T extends InventoryItem> extends ItemUpdater<T> {

    /**
     * Update all objects in inventory
     *
     * @param builder The @{@link InventoryBuilder}
     * @param inventory The @{@link Inventory}
     */
    void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory);

    void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory,
                @NotNull T object, int slot);

    /**
     * Update object in inventory
     *
     * @param builder The @{@link InventoryBuilder}
     * @param inventory The @{@link Inventory}
     * @param slot The slot to update
     * @param object The object to update
     * @param callback The @{@link ObjectCallback}
     */
    void update(@NotNull InventoryBuilder<T> builder, @NotNull Inventory inventory,
                @NotNull ObjectCallback<T> callback, @NotNull T object, int slot);
}
