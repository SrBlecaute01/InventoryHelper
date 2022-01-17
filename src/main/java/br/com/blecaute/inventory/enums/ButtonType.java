package br.com.blecaute.inventory.enums;

import br.com.blecaute.inventory.InventoryBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum of buttons to skip or back page of @{@link InventoryBuilder}
 */
@AllArgsConstructor
public enum ButtonType {

    PREVIOUS_PAGE(-1),
    NEXT_PAGE(1);

    @Getter private final int value;
}