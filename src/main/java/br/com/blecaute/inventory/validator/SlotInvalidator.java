package br.com.blecaute.inventory.validator;

import org.bukkit.inventory.Inventory;

/**
 * The interface to invalidate slots of @{@link Inventory}
 */
@FunctionalInterface
public interface SlotInvalidator {

    boolean validate(int slot);

}