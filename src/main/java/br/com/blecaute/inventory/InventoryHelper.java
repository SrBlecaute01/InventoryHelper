package br.com.blecaute.inventory;

import br.com.blecaute.inventory.listener.InventoryClickListener;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/**
 * Class for initializing the inventory helper.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InventoryHelper {

    @Getter private static boolean enabled = false;
    @Getter private static InventoryManager manager = null;

    /**
     * Enable inventory helper.
     *
     * @param plugin The @{@link Plugin}
     */
    public static void enable(Plugin plugin) {
        if (enabled) return;

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new InventoryClickListener(), plugin);

        manager = new InventoryManager(plugin);
        enabled = true;
    }

}
