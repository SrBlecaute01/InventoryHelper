package br.com.blecaute.inventory;

import br.com.blecaute.inventory.type.InventoryItemType;
import lombok.Data;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
public class InventoryClick<T extends InventoryItemType> {

    @NotNull private final InventoryClickEvent event;
    @NotNull private final ItemStack itemStack;

    /**
     * this value can be null if it does not exist
     */
    @Nullable
    private final T value;
}
