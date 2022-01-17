package br.com.blecaute.inventory.event;

import br.com.blecaute.inventory.type.InventoryItem;
import lombok.Data;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
public class InventoryClick<T extends InventoryItem> {

    @NotNull private final InventoryClickEvent event;
    @NotNull private final ItemStack itemStack;

    @Nullable
    private final T object;
}