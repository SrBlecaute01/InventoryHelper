package br.com.blecaute.inventory;

import lombok.Data;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.function.Consumer;

@Data
public class CustomInventoryHolder implements InventoryHolder {

    private final Consumer<InventoryEvent> consumer;

    @Override
    public Inventory getInventory() {
        return null;
    }
}
