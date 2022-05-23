package br.com.blecaute.inventory.callback;

import br.com.blecaute.inventory.event.ItemClickEvent;
import br.com.blecaute.inventory.type.InventoryItem;

/**
 * The ItemCallback is designed to process the click event
 * of an item in inventories.
 *
 * @param <T> The type of InventoryItem.
 */
public interface ItemCallback<T extends InventoryItem> extends InventoryCallback<T, ItemClickEvent<T>> {

    ItemCallback<?> EMPTY = click -> {};

    /**
     * Get an empty ItemCallback.
     *
     * @param <T> The type of InventoryItem.
     *
     * @return The empty ItemCallback.
     */
    @SuppressWarnings("unchecked cast")
    static <T extends InventoryItem> ItemCallback<T> empty() {
        return (ItemCallback<T>) EMPTY;
    }

}