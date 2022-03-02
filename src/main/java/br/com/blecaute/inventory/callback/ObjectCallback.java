package br.com.blecaute.inventory.callback;

import br.com.blecaute.inventory.event.ObjectClickEvent;
import br.com.blecaute.inventory.type.InventoryItem;

public interface ObjectCallback<T extends InventoryItem> extends InventoryCallback<T, ObjectClickEvent<T>> { }