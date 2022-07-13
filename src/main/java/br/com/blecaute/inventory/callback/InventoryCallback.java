package br.com.blecaute.inventory.callback;

import br.com.blecaute.inventory.event.InventoryEvent;
import br.com.blecaute.inventory.type.InventoryItem;

/**
 * The InventoryCallback is designed to process the click event in inventories.
 *
 * @param <S> The type of InventoryItem.
 * @param <T> The type of InventoryEvent.
 */
@FunctionalInterface
public interface InventoryCallback<S extends InventoryItem, T extends InventoryEvent<S>> {

    /**
     * Accept click event.
     *
     * @param click The type of InventoryEvent.
     */
    void accept(T click);

}