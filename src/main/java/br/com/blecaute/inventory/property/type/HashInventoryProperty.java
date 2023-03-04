package br.com.blecaute.inventory.property.type;

import br.com.blecaute.inventory.exception.InventoryBuilderException;
import br.com.blecaute.inventory.property.InventoryProperty;

import java.util.HashMap;

public class HashInventoryProperty extends AbstractInventoryProperty implements Cloneable {

    public HashInventoryProperty() {
        super(new HashMap<>());
    }

    @Override
    public InventoryProperty deepClone() {
        return clone();
    }

    @Override
    public HashInventoryProperty clone() {
        try {
            HashInventoryProperty property = (HashInventoryProperty) super.clone();
            property.map = new HashMap<>(this.map);

            return property;

        } catch (CloneNotSupportedException exception) {
            throw new InventoryBuilderException(exception.getMessage());
        }
    }

}
