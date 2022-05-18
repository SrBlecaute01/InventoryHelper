package br.com.blecaute.inventory.event;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.event.updatable.UpdatableObjectPaginated;
import br.com.blecaute.inventory.format.InventoryFormat;
import br.com.blecaute.inventory.format.impl.PaginatedObjectFormat;
import br.com.blecaute.inventory.format.updater.PaginatedObjectUpdater;
import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class PaginatedObjectEvent<T extends InventoryItem> extends ObjectClickEvent<T> implements UpdatableObjectPaginated<T> {

    public PaginatedObjectEvent(@NotNull InventoryFormat<T> format,
                                @NotNull InventoryBuilder<T> builder,
                                @NotNull InventoryClickEvent event,
                                @Nullable T object) {

        super(format, builder, event, object);
    }

    @Override
    public void update(@NotNull Collection<T> objects) {
        if (!(format instanceof PaginatedObjectUpdater)) {
            throw new UnsupportedOperationException("The format is not an instance of PaginatedObjectUpdater");
        }

        ((PaginatedObjectUpdater<T>) format).update(getBuilder(), getInventory(), objects);
    }

}
