package br.com.blecaute.inventory.event.updatable;

import br.com.blecaute.inventory.callback.ObjectCallback;
import br.com.blecaute.inventory.type.InventoryItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface UpdatableObject<T extends InventoryItem> extends Updatable {

    void update();

    void update(@NotNull T object);

    void update(@NotNull T object, @Nullable ObjectCallback<T> callback);

    void update(int slot, @NotNull T object, @Nullable ObjectCallback<T> callback);

}
