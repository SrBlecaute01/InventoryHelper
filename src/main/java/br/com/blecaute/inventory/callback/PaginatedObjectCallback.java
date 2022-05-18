package br.com.blecaute.inventory.callback;

import br.com.blecaute.inventory.event.PaginatedObjectEvent;
import br.com.blecaute.inventory.type.InventoryItem;

public interface PaginatedObjectCallback<T extends InventoryItem> extends InventoryCallback<T, PaginatedObjectEvent<T>> {}
