package br.com.blecaute.inventory.callback;

import br.com.blecaute.inventory.InventoryUpdater;
import br.com.blecaute.inventory.type.InventoryItem;
import org.jetbrains.annotations.NotNull;

/**
 * The UpdateCallback is designed to process updates in the inventory.
 *
 * @param <T> The type of InventoryItem.
 */
@FunctionalInterface
public interface UpdateCallback<T extends InventoryItem> {

    void update(@NotNull InventoryUpdater<T> updater);

}