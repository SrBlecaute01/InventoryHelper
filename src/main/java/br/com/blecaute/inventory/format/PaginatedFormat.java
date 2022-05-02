package br.com.blecaute.inventory.format;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * Interface to create paginated inventory in @{@link InventoryBuilder}
 */
public interface PaginatedFormat<T extends InventoryItem> extends InventoryFormat<T> {

    /**
     * Get size of objects.
     * @return The size of objects
     */
    int getSize();

    /**
     * Format inventory
     *
     * @param inventory     The @{@link Inventory}
     * @param builder       The @{@link InventoryBuilder}
     * @param skipFunction  The @{@link Function} to check slots.
     */
    void format(@NotNull Inventory inventory,
                @NotNull InventoryBuilder<T> builder,
                @Nullable Function<Integer, Boolean> skipFunction);

    @Override
    default void format(@NotNull Inventory inventory, @NotNull InventoryBuilder<T> builder) {}
}