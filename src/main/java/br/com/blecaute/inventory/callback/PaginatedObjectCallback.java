package br.com.blecaute.inventory.callback;

import br.com.blecaute.inventory.event.PaginatedObjectEvent;
import br.com.blecaute.inventory.type.InventoryItem;

/**
 * The PaginatedObjectCallback is designed to process the click
 * on an item that referenced an object in a paginated inventory.
 *
 * @param <T> The type of InventoryItem.
 */
public interface PaginatedObjectCallback<T extends InventoryItem> extends InventoryCallback<T, PaginatedObjectEvent<T>> {}
