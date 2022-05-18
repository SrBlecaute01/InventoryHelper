package br.com.blecaute.inventory.callback;

import br.com.blecaute.inventory.event.PaginatedItemEvent;
import br.com.blecaute.inventory.type.InventoryItem;

public interface PaginatedItemCallback<T extends InventoryItem> extends InventoryCallback<T, PaginatedItemEvent<T>> {}