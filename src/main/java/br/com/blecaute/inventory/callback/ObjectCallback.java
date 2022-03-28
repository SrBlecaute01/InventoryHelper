package br.com.blecaute.inventory.callback;

import br.com.blecaute.inventory.event.ObjectClickEvent;
import br.com.blecaute.inventory.type.InventoryItem;

/**
 * The interface responsible for returning click objects.
 *
 * @param <T> The type of @{@link InventoryItem}
 */
public interface ObjectCallback<T extends InventoryItem> extends InventoryCallback<T, ObjectClickEvent<T>> {}