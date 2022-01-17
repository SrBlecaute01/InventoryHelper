package br.com.blecaute.inventory.format;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.InventoryClick;
import br.com.blecaute.inventory.property.InventoryProperty;
import br.com.blecaute.inventory.type.InventoryItemType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Interface to formats of inventories
 */
public interface InventoryFormat<T extends InventoryItemType> {

    boolean isValid(int slot);

    void accept(@NotNull InventoryClickEvent event, @NotNull InventoryBuilder<T> builder);

    default void accept(@Nullable Consumer<InventoryClick<T>> consumer, @NotNull InventoryClick<T> click) {
        if (consumer != null) {
            consumer.accept(click);
        }
    }

    void format(@NotNull Inventory inventory, @NotNull InventoryProperty property);

}