package br.com.blecaute.inventory.buttons;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.format.InventoryFormat;
import br.com.blecaute.inventory.type.InventorySlot;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public interface Button extends InventorySlot {

    void accept(@NotNull InventoryClickEvent event, @NotNull InventoryBuilder<?> builder, @NotNull InventoryFormat<?> format);

    boolean canPlace(@NotNull InventoryBuilder<?> builder, @NotNull InventoryFormat<?> format);

}