package br.com.blecaute.inventory.callback;

import br.com.blecaute.inventory.InventoryUpdater;
import br.com.blecaute.inventory.type.InventoryItem;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface UpdateCallback<T extends InventoryItem> {

    void update(@NotNull InventoryUpdater<T> updater);

}