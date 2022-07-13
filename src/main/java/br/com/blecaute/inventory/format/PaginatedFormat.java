package br.com.blecaute.inventory.format;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.configuration.PaginatedConfiguration;
import br.com.blecaute.inventory.type.InventoryItem;
import br.com.blecaute.inventory.util.ListUtil;

import java.util.List;
import java.util.Map;

/**
 * Interface to create paginated inventory in @{@link InventoryBuilder}
 */
public interface PaginatedFormat<T extends InventoryItem> extends InventoryFormat<T> {

    PaginatedConfiguration getConfiguration();

    /**
     * Get size of objects.
     * @return The size of objects
     */
    int getSize();

    /**
     * Get amount of pages
     * @return The amount of pages
     */
    int getPages();

    /**
     * Calculate pages
     *
     * @param size The size of objects in each page
     * @param items The items
     * @param map The map of pages
     * @param <S> The @{@link InventoryFormat}
     */
    default <S extends InventoryFormat<T>> void calculate(int size, List<S> items, Map<Integer, List<S>> map) {
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