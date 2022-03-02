package br.com.blecaute.inventory.event;

import br.com.blecaute.inventory.property.InventoryProperty;
import br.com.blecaute.inventory.type.InventoryItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ObjectClickEvent<T extends InventoryItem> extends ItemClickEvent<T> {

    private final T object;

    public ObjectClickEvent(@NotNull InventoryClickEvent event, @NotNull ItemStack itemStack,
                            @NotNull InventoryProperty properties, @Nullable T object) {

        super(event, itemStack, properties);
        this.object = object;
    }

    @Override
    @SuppressWarnings("deprecation")
    public @Nullable T getObject() {
        return object;
    }
}
