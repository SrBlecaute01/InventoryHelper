package br.com.blecaute.inventory.callback;

import br.com.blecaute.inventory.event.PaginatedItemEvent;
import br.com.blecaute.inventory.type.InventoryItem;

/**
 * The PaginatedItemCallback is designed to process the click
 * in an item with paginated inventory.
 *
 * @param <T> The type of InventoryItem.
 */
public interface PaginatedItemCallback<T extends InventoryItem> extends InventoryCallback<T, PaginatedItemEvent<T>> {}