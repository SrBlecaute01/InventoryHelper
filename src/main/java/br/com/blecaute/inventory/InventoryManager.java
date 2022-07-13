package br.com.blecaute.inventory;

import br.com.blecaute.inventory.task.InventoryUpdateTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class InventoryManager {

    private final Set<InventoryBuilder<?>> inventories = ConcurrentHashMap.newKeySet();

    public InventoryManager(Plugin plugin) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new InventoryUpdateTask(), 20L, 20L);
    }

    public void register(InventoryBuilder<?> builder) {
        inventories.add(builder);
    }

    public void unregister(InventoryBuilder<?> builder) {
        inventories.remove(builder);
    }

    public Set<InventoryBuilder<?>> getInventories() {
        return inventories;
    }

    public void updateInventories(int seconds) {
        for (InventoryBuilder<?> builder : getInventories()) {
            if (builder.getInventory().getViewers().size() <= 0) {
                unregister(builder);
                continue;
            }

            builder.handleUpdates(seconds);
        }
    }

}
