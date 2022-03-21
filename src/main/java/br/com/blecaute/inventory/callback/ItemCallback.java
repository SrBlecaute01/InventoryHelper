package br.com.blecaute.inventory.callback;

import br.com.blecaute.inventory.event.ItemClickEvent;
import br.com.blecaute.inventory.type.InventoryItem;

/**
 * The interface responsible for returning the click on items.
 *
 * @param <T> The type of @{@link InventoryItem}
 */
public interface ItemCallback<T extends InventoryItem> extends InventoryCallback<T, ItemClickEvent<T>> {

    ItemCallback<?> EMPTY = click -> {};

    @SuppressWarnings("unchecked cast")
    static <T extends InventoryItem> ItemCallback<T> empty() {
        return (ItemCallback<T>) EMPTY;
    }

}