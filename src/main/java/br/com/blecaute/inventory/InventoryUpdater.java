package br.com.blecaute.inventory;

import br.com.blecaute.inventory.callback.ItemCallback;
import br.com.blecaute.inventory.callback.ObjectCallback;
import br.com.blecaute.inventory.callback.PaginatedItemCallback;
import br.com.blecaute.inventory.callback.PaginatedObjectCallback;
import br.com.blecaute.inventory.configuration.PaginatedConfiguration;
import br.com.blecaute.inventory.event.updatable.Updatable;
import br.com.blecaute.inventory.exception.InventoryBuilderException;
import br.com.blecaute.inventory.format.InventoryFormat;
import br.com.blecaute.inventory.format.PaginatedFormat;
import br.com.blecaute.inventory.format.impl.PaginatedItemFormatImpl;
import br.com.blecaute.inventory.format.impl.SimpleItemFormatImpl;
import br.com.blecaute.inventory.format.impl.SimpleObjectFormatImpl;
import br.com.blecaute.inventory.format.updater.ItemUpdater;
import br.com.blecaute.inventory.format.updater.ObjectUpdater;
import br.com.blecaute.inventory.format.updater.PaginatedObjectUpdater;
import br.com.blecaute.inventory.property.InventoryProperty;
import br.com.blecaute.inventory.type.InventoryItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

@RequiredArgsConstructor
public class InventoryUpdater<T extends InventoryItem> implements Updatable<T> {

    @Getter(AccessLevel.PRIVATE)
    private final InventoryBuilder<T> builder;

    @Contract("_ -> new")
    public static <T extends InventoryItem> @NotNull InventoryUpdater<T> of(InventoryBuilder<T> builder) {
        return new InventoryUpdater<>(builder);
    }

    @NotNull
    public InventoryProperty getProperties() {
        return builder.getProperties();
    }

    @NotNull
    public Inventory getInventory() {
        return builder.getInventory();
    }

    @Override
    public void update() {
        this.builder.format();
    }

    @Override
    public void updateItem(int slot, @Nullable ItemStack item) {
        validateSlot(slot);

        InventoryFormat<T> format = this.getFormat(slot);
        if (format != null) {
            getItemUpdater(format).update(builder, getInventory(), item, slot);
            return;
        }

        SimpleItemFormatImpl<T> simpleFormat = new SimpleItemFormatImpl<>(slot, item, null);
        simpleFormat.format(getInventory(), builder);

        builder.addFormat(simpleFormat);
    }

    @Override
    public void updateItem(int slot, @Nullable ItemStack item, @Nullable ItemCallback<T> callback) {
        validateSlot(slot);

        InventoryFormat<T> format = this.getFormat(slot);
        if (format != null) {
            getItemUpdater(format).update(builder, getInventory(), callback, item, slot);
            return;
        }

        SimpleItemFormatImpl<T> simpleFormat = new SimpleItemFormatImpl<>(slot, item, callback);
        simpleFormat.format(getInventory(), builder);

        builder.addFormat(simpleFormat);
    }

    @Override
    public void updateItems(@NotNull String identifier, @NotNull Collection<ItemStack> items) {
        getPaginatedItemUpdater(this.getFormat(identifier)).update(builder, getInventory(), items);
    }

    @Override
    public void updateItems(@NotNull String identifier, @NotNull Collection<ItemStack> items, @Nullable PaginatedItemCallback<T> callback) {
        getPaginatedItemUpdater(this.getFormat(identifier)).update(builder, getInventory(), items, callback);
    }

    @Override
    public void updateObject(int slot, @NotNull T object) {
        validateSlot(slot);

        InventoryFormat<T> format = this.getFormat(slot);
        if (format != null) {
            getObjectUpdater(format).update(builder, getInventory(), object, slot);
            return;
        }

        SimpleObjectFormatImpl<T> simpleFormat = new SimpleObjectFormatImpl<>(slot, object, null);
        simpleFormat.format(getInventory(), builder);

        builder.addFormat(simpleFormat);
    }

    @Override
    public void updateObject(int slot, @NotNull T object, @Nullable ObjectCallback<T> callback) {
        validateSlot(slot);

        InventoryFormat<T> format = this.getFormat(slot);
        if (format != null) {
            getObjectUpdater(format).update(builder, getInventory(), callback, object, slot);
            return;
        }

        SimpleObjectFormatImpl<T> simpleFormat = new SimpleObjectFormatImpl<>(slot, object, null);
        simpleFormat.format(getInventory(), builder);

        builder.addFormat(simpleFormat);
    }

    @Override
    public void updateObjects(@NotNull String identifier, @NotNull Collection<T> objects) {
        getPaginatedObjectFormat(this.getFormat(identifier)).update(builder, getInventory(), objects);
    }

    @Override
    public void updateObjects(@NotNull String identifier, @NotNull Collection<T> objects,
                              @Nullable PaginatedObjectCallback<T> callback) {

        getPaginatedObjectFormat(this.getFormat(identifier)).update(builder, getInventory(), objects, callback);
    }

    @SuppressWarnings("unchecked")
    private PaginatedItemFormatImpl<T> getPaginatedItemUpdater(InventoryFormat<T> format) {
        return (PaginatedItemFormatImpl<T>) getUpdater(format, PaginatedItemFormatImpl.class);
    }

    @SuppressWarnings("unchecked")
    private PaginatedObjectUpdater<T> getPaginatedObjectFormat(InventoryFormat<T> format) {
        return (PaginatedObjectUpdater<T>) getUpdater(format, PaginatedObjectUpdater.class);
    }

    @SuppressWarnings("unchecked")
    private ItemUpdater<T> getItemUpdater(InventoryFormat<T> format) {
        return (ItemUpdater<T>) getUpdater(format, ItemUpdater.class);
    }

    @SuppressWarnings("unchecked")
    private ObjectUpdater<T> getObjectUpdater(InventoryFormat<T> format) {
        return (ObjectUpdater<T>) getUpdater(format, ObjectUpdater.class);
    }


    private <R> R getUpdater(@NotNull InventoryFormat<T> format, @NotNull Class<R> clazz) {
        if (clazz.isAssignableFrom(format.getClass())) {
            return clazz.cast(format);
        }

        throw new InventoryBuilderException(
                "Unable to update inventory because the provided InventoryFormat is not an instance of " + clazz.getSimpleName()
        );
    }

    private void validateSlot(int slot) {
        int size = getInventory().getSize();
        if (slot < 0 || slot >= size) {
            throw new InventoryBuilderException(
                    "Cannot update item with given slot: " + slot + " for inventory with size: " + size
            );
        }
    }

    @Nullable
    private InventoryFormat<T> getFormat(int slot) {
        return this.builder.getFormats().stream()
                .filter(format -> format.isValid(slot))
                .findFirst()
                .orElse(null);
    }

    private @NotNull PaginatedFormat<T> getFormat(String identifier) {
        for (InventoryFormat<T> format : this.builder.getFormats()) {
            if (!(format instanceof PaginatedFormat)) continue;

            PaginatedFormat<T> paginated = (PaginatedFormat<T>) format;
            PaginatedConfiguration configuration = paginated.getConfiguration();

            if (configuration.getIdentifier().equals(identifier)) {
                return paginated;
            }
        }

        throw new InventoryBuilderException("Could not find format for identifier: " + identifier);
    }

}
