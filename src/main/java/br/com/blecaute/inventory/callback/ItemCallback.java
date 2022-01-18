package br.com.blecaute.inventory.callback;

import br.com.blecaute.inventory.event.InventoryEvent;
import br.com.blecaute.inventory.type.InventoryItem;

@FunctionalInterface
public interface ItemCallback<T extends InventoryItem> {

    void accept(InventoryEvent<T> click);

}
