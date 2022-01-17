package br.com.blecaute.inventory.listener;

import br.com.blecaute.inventory.InventoryBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory() == null) return;

        InventoryHolder inventoryHolder = event.getInventory().getHolder();
        if (inventoryHolder instanceof InventoryBuilder.CustomHolder) {
            InventoryBuilder.CustomHolder holder = (InventoryBuilder.CustomHolder) inventoryHolder;
            ItemStack item = event.getCurrentItem();

            if (item != null && item.getType() != Material.AIR) {
                holder.getConsumer().accept(event);
            }

            event.setCancelled(true);
        }

    }

}
