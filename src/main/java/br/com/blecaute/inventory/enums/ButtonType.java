package br.com.blecaute.inventory.enums;

import br.com.blecaute.inventory.InventoryBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;

/**
 * Enum of buttons to skip or back page of @{@link InventoryBuilder}
 */
@AllArgsConstructor
@Deprecated
@ApiStatus.ScheduledForRemoval
public enum ButtonType {

    PREVIOUS_PAGE(-1),
    NEXT_PAGE(1);

    @Getter
    private final int value;
}
