package br.com.blecaute.inventory.callback;

import br.com.blecaute.inventory.event.ObjectClickEvent;
import br.com.blecaute.inventory.type.InventoryItem;

/**
 * The ObjectCallback is designed to process the click
 * on an item in inventory that references an object.
 *
 * @param <T> The type of InventoryItem.
 */
public interface ObjectCallback<T extends InventoryItem> extends InventoryCallback<T, ObjectClickEvent<T>> {}