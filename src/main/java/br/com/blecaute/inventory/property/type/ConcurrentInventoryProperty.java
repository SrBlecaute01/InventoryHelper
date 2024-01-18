package br.com.blecaute.inventory.property.type;

import br.com.blecaute.inventory.exception.InventoryBuilderException;
import br.com.blecaute.inventory.property.InventoryProperty;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentInventoryProperty extends AbstractInventoryProperty implements Cloneable {

    public ConcurrentInventoryProperty() {
        super(new ConcurrentHashMap<>());
    }

    @Override
    public InventoryProperty deepClone() {
        return clone();
    }

    @Override
    public ConcurrentInventoryProperty clone() {
        try {
            ConcurrentInventoryProperty property = (ConcurrentInventoryProperty) super.clone();
            property.map = new ConcurrentHashMap<>(this.map);

            return property;

        } catch (CloneNotSupportedException exception) {
            throw new InventoryBuilderException(exception.getMessage());
        }
    }
}
