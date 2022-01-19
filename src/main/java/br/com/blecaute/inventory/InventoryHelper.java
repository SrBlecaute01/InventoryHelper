package br.com.blecaute.inventory;

import br.com.blecaute.inventory.listener.InventoryClickListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/**
 * Class for initializing the inventory helper.
 */
public class InventoryHelper {

    @Getter private static boolean enabled = false;

    /**
     * Prevent invalid instance of object.
     */
    private InventoryHelper() {}

    /**
     * Enable inventory helper.
     *
     * @param plugin The @{@link Plugin}
     */
    public static void enable(Plugin plugin) {
        if (enabled) return;

        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new InventoryClickListener(), plugin);

        enabled = true;
    }


}
