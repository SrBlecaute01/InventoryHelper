package br.com.blecaute.inventory.property.type;

import br.com.blecaute.inventory.exception.InventoryBuilderException;
import br.com.blecaute.inventory.property.InventoryProperty;

import java.util.WeakHashMap;

public class WeakInventoryProperty extends AbstractInventoryProperty implements Cloneable {

    public WeakInventoryProperty() {
        super(new WeakHashMap<>());
    }

    @Override
    public InventoryProperty deepClone() {
        return clone();
    }

    @Override
    public WeakInventoryProperty clone() {
        try {
            WeakInventoryProperty property = (WeakInventoryProperty) super.clone();
            property.map = new WeakHashMap<>(this.map);

            return property;

        } catch (CloneNotSupportedException exception) {
            throw new InventoryBuilderException(exception.getMessage());
        }
    }
}
