package br.com.blecaute.inventory.event;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.callback.PaginatedObjectCallback;
import br.com.blecaute.inventory.event.updatable.UpdatableObjectPaginated;
import br.com.blecaute.inventory.format.InventoryFormat;
import br.com.blecaute.inventory.format.PaginatedFormat;
import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class PaginatedObjectEvent<T extends InventoryItem> extends ObjectClickEvent<T> implements UpdatableObjectPaginated<T> {

    public PaginatedObjectEvent(@NotNull InventoryFormat<T> format,
                                @NotNull InventoryBuilder<T> builder,
                                @NotNull InventoryClickEvent event,
                                @Nullable T object) {

        super(format, builder, event, object);
    }

    @Override
    public void update(@NotNull Collection<T> objects) {
        updater.updateObjects(getIdentifier(), objects);
    }

    @Override
    public void update(@NotNull Collection<T> objects, @NotNull PaginatedObjectCallback<T> callback) {
        updater.updateObjects(getIdentifier(), objects, callback);
    }

    private @NotNull String getIdentifier() {
        return ((PaginatedFormat<T>) format).getConfiguration().getIdentifier();
    }

}
