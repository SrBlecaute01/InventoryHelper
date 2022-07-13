package br.com.blecaute.inventory.exception;

/**
 * InventoryBuilderException is a runtime exception called
 * when an error is encountered in InventoryBuilder.
 */
public class InventoryBuilderException extends RuntimeException {

    public InventoryBuilderException(String message) {
        super(message);
    }

    public InventoryBuilderException(Exception exception) {
        super(exception.getMessage());
    }

}