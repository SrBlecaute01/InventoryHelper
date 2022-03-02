package br.com.blecaute.inventory.callback;

import br.com.blecaute.inventory.event.InventoryEvent;
import br.com.blecaute.inventory.type.InventoryItem;

public interface InventoryCallback<I extends InventoryItem, T extends InventoryEvent<I>> {

    void accept(T click);

}