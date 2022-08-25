package br.com.blecaute.inventory;

import br.com.blecaute.inventory.buttons.Button;
import br.com.blecaute.inventory.configuration.PaginatedConfiguration;
import br.com.blecaute.inventory.format.InventoryFormat;
import br.com.blecaute.inventory.listener.InventoryClickListener;
import br.com.blecaute.inventory.property.InventoryProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
