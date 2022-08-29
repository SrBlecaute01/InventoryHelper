package br.com.blecaute.inventory.format;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.configuration.PaginatedConfiguration;
import br.com.blecaute.inventory.type.InventoryItem;
import br.com.blecaute.inventory.util.ListUtil;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * Represents a paginated inventory format
 * @param <T> The @{@link InventoryItem} type.
 */
public interface PaginatedFormat<T extends InventoryItem> extends InventoryFormat<T> {

    /**
     * Get the @{@link PaginatedConfiguration} for this formatter.
     *
     * @return The paginated configuration.
     */
    @NotNull
    PaginatedConfiguration getConfiguration();

    /**
     * Get size of objects in this formatter.
     *
     * @return The objects size.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    int getSize();

    /**
     * Get size of objects in this formatter.
     *
     * @return The objects size.
     */
    int getObjectsSize();

    /**
     * Get number of pages.
     *
     * @return The number of pages.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    int getPages();

    /**
     * Get pages number.
     *
     * @return The pages number.
     */
    int getPagesSize();

    /**
     * Get current page number.
     *
     * @return The current page number.
     */
    int getCurrentPage();

    /**
     * Set current page number.
     *
     * @param inventory The inventory.
     * @param builder The builder.
     * @param page The page.
     */
    void setCurrentPage(@NotNull Inventory inventory, @NotNull InventoryBuilder<T> builder, int page);

    /**
     * Calculate pages number.
     *
     * @param size The size of objects in each page.
     * @param items The items.
     * @param map The map of pages.
     *
     * @param <S> The @{@link InventoryFormat} type.
     */
    default <S extends InventoryFormat<T>> void calculate(int size, List<S> items, @NotNull Map<Integer, List<S>> map) {
        if (map.size() > 0) return;
        if (size <= 0) {
            map.put(1, items);
            return;
        }

        for (int page = 0; items.size() > page * size; page++) {
            map.put(page + 1, ListUtil.getSublist(items, page + 1, size));
        }
    }
}