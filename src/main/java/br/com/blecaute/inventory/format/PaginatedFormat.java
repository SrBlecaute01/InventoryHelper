package br.com.blecaute.inventory.format;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public interface PaginatedFormat<T extends InventoryItem> extends InventoryFormat<T> {

    int getSize();

    void format(@NotNull Inventory inventory,
                @NotNull InventoryBuilder<T> builder,
                @Nullable Function<Integer, Boolean> skipFunction);

    @Override
    default void format(@NotNull Inventory inventory, @NotNull InventoryBuilder<T> builder) {}
}