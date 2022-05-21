package br.com.blecaute.inventory.event.updatable;

import br.com.blecaute.inventory.callback.PaginatedObjectCallback;
import br.com.blecaute.inventory.type.InventoryItem;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface UpdatableObjectPaginated<T extends InventoryItem> extends UpdatableObject<T> {

    void update(@NotNull Collection<T> objects);

    void update(@NotNull Collection<T> objects, @NotNull PaginatedObjectCallback<T> callback);

}
