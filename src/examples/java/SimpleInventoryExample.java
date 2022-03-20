import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.InventoryHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleInventoryExample extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // enable inventory helper
        InventoryHelper.enable(this);

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        new InventoryBuilder<>("Custom inventory", 3)
                .withItem(13, getItem(), click -> {
                    player.closeInventory();
                    player.sendMessage("§eYou clicked in item ;).");
                }).build(player);
    }

    public ItemStack getItem() {
        ItemStack itemStack = new ItemStack(Material.DIAMOND);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("§eClick");

        itemStack.setItemMeta(meta);

        return itemStack;
    }

}
