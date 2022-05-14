package br.com.blecaute.inventory.event.updatable;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface Updatable {

    void update(int slot, @NotNull ItemStack itemStack);

}
