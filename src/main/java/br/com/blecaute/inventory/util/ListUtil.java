package br.com.blecaute.inventory.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ListUtil {

    /**
     * get paginated list
     *
     * @param list the list
     * @param value the page
     * @param size the size of list
     * @return the list
     */
    public static <E> @NotNull List<E> getSublist(@NotNull List<E> list, int value, int size) {
        if(list.isEmpty()) return list;

        int first = Math.min(value * size - size, list.size() - 1);
        int end = Math.min(list.size(), first + size);

        return list.subList(first, end);
    }
}