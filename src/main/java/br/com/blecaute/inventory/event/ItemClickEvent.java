package br.com.blecaute.inventory.event;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.callback.ItemCallback;
import br.com.blecaute.inventory.callback.ObjectCallback;
import br.com.blecaute.inventory.callback.PaginatedItemCallback;
import br.com.blecaute.inventory.callback.PaginatedObjectCallback;
import br.com.blecaute.inventory.event.updatable.UpdatableItem;
import br.com.blecaute.inventory.format.InventoryFormat;
import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * The event called when the player clicks on an item.
 *
 * @param <T> The type of @{@link InventoryItem}
 */
public class ItemClickEvent<T extends InventoryItem> extends InventoryEvent<T> implements UpdatableItem<T> {

    protected final InventoryFormat<T> format;

    /**
     * Create a new InventoryEvent with the given InventoryFormat, InventoryBuilder and InventoryClickEvent.
     *
     * @param format The @{@link InventoryFormat}
     * @param builder The @{@link InventoryBuilder}
     * @param event The @{@link InventoryClickEvent}
     */
    public ItemClickEvent(@NotNull InventoryFormat<T> format,
                          @NotNull InventoryBuilder<T> builder,
                          @NotNull InventoryClickEvent event) {

        super(builder, event);
        this.format = format;
    }

    @Override
    public void update() {
        updater.update();
    }

    @Override
    public void updateItem(int slot, @Nullable ItemStack itemStack) {
        updater.updateItem(slot, itemStack);
    }

    @Override
    public void updateItem(int slot, @Nullable ItemStack itemStack, @Nullable ItemCallback<T> callback) {
        updater.updateItem(slot, itemStack, callback);
    }

    @Override
    public void updateItems(@NotNull String identifier, @NotNull Collection<ItemStack> items) {
        updater.updateItems(identifier, items);
    }

    @Override
    public void updateItems(@NotNull String identifier, @NotNull Collection<ItemStack> items, @Nullable PaginatedItemCallback<T> callback) {
        updater.updateItems(identifier, items, callback);
    }

    @Override
    public void updateObject(int slot, @NotNull T object) {
        updater.updateObject(slot, object);
    }

    @Override
    public void updateObject(int slot, @NotNull T object, @Nullable ObjectCallback<T> callback) {
        updater.updateObject(slot, object, callback);
    }

    @Override
    public void updateObjects(@NotNull String identifier, @NotNull Collection<? extends T> objects) {
        updater.updateObjects(identifier, objects);
    }

    @Override
    public void updateObjects(@NotNull String identifier, @NotNull Collection<? extends T> objects, @Nullable PaginatedObjectCallback<T> callback) {
        updater.updateObjects(identifier, objects, callback);
    }

    @Override
    public void updateItem(@Nullable ItemStack itemStack) {
        updateItem(getSlot(), itemStack);
    }

    @Override
    public void updateItem(@Nullable ItemStack itemStack, @NotNull ItemCallback<T> callback) {
        updateItem(getSlot(), itemStack, callback);
    }
}
