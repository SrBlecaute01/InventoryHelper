package br.com.blecaute.inventory.validator;

import org.bukkit.inventory.Inventory;

/**
 * The interface to validate slots of @{@link Inventory}
 */
@FunctionalInterface
public interface SlotValidator {

    boolean validate(int slot);

}