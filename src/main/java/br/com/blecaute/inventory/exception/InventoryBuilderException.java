package br.com.blecaute.inventory.exception;

import br.com.blecaute.inventory.InventoryBuilder;

/**
 * InventoryBuilderException is a runtime exception called
 * when an error is encountered in @{@link InventoryBuilder}
 */
public class InventoryBuilderException extends RuntimeException {

    public InventoryBuilderException(String message) {
        super(message);
    }

    public InventoryBuilderException(Exception exception) {
        super(exception.getMessage());
    }

}
