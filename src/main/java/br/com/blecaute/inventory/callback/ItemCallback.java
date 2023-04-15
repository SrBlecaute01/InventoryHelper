package br.com.blecaute.inventory.callback;

import br.com.blecaute.inventory.event.ItemClickEvent;
import br.com.blecaute.inventory.type.InventoryItem;

/**
 * The ItemCallback is designed to process the click event
 * of an item in inventories.
 *
 * @param <T> The type of InventoryItem.
 */
public interface ItemCallback<T extends InventoryItem> extends InventoryCallback<T, ItemClickEvent<T>> {}