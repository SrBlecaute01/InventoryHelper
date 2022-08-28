package br.com.blecaute.inventory.exception;

import org.jetbrains.annotations.NotNull;

/**
 * InventoryBuilderException is a runtime exception called
 * when an error is encountered in InventoryBuilder.
 */
public class InventoryBuilderException extends RuntimeException {

    public InventoryBuilderException(String message) {
        super(message);
    }

    public InventoryBuilderException(@NotNull Exception exception) {
        super(exception.getMessage());
    }

}