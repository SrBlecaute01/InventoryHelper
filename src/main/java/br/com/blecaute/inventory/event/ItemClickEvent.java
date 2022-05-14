package br.com.blecaute.inventory.event;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.callback.ItemCallback;
import br.com.blecaute.inventory.event.updatable.UpdatableItem;
import br.com.blecaute.inventory.format.InventoryFormat;
import br.com.blecaute.inventory.format.updater.ItemUpdater;
import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The event called when the player clicks on an item.
 *
 * @param <T> The type of @{@link InventoryItem}
 */
public class ItemClickEvent<T extends InventoryItem> extends InventoryEvent<T> implements UpdatableItem<T> {

    private final InventoryFormat<T> format;

    public ItemClickEvent(@NotNull InventoryFormat<T> format,
                          @NotNull InventoryBuilder<T> builder,
                          @NotNull InventoryClickEvent event) {

        super(builder, event);
        this.format = format;
    }

    @Override
    public void update(int slot, @NotNull ItemStack itemStack) {
        getUpdater().update(getBuilder(), getInventory(), itemStack, slot);
    }

    @Override
    public void update(@NotNull ItemStack itemStack) {
        getUpdater().update(getBuilder(), getInventory(), itemStack, getSlot());
    }

    @Override
    public void update(@NotNull ItemStack itemStack, @Nullable ItemCallback<T> callback) {
        getUpdater().update(getBuilder(), getInventory(), callback, itemStack, getSlot());
    }

    @Override
    public void update(int slot, @NotNull ItemStack itemStack, @Nullable ItemCallback<T> callback) {
        getUpdater().update(getBuilder(), getInventory(), callback, itemStack, slot);
    }

    @SuppressWarnings("unchecked")
    private ItemUpdater<T> getUpdater() {
        if (!(format instanceof ItemUpdater)) {
            throw new UnsupportedOperationException("The format is not an instance of ItemUpdater");
        }

        return (ItemUpdater<T>) format;
    }

}
