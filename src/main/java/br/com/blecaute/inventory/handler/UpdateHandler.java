package br.com.blecaute.inventory.handler;

import br.com.blecaute.inventory.InventoryUpdater;
import br.com.blecaute.inventory.callback.UpdateCallback;
import br.com.blecaute.inventory.type.InventoryItem;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class UpdateHandler<T extends InventoryItem> {

    private final long seconds;
    private final UpdateCallback<T> callback;

    public UpdateHandler(long time, @NotNull TimeUnit unit, @NotNull UpdateCallback<T> callback) {
        Validate.isTrue(time > 0, "time must be greater than 0");
        Validate.notNull(unit, "unit cannot be null");
        Validate.notNull(callback, "callback cannot be null");

        this.seconds = unit.toSeconds(time);
        this.callback = callback;
    }

    public void call(InventoryUpdater<T> updater) {
        Validate.notNull(updater, "updater cannot be null");
        callback.update(updater);
    }

    public boolean canUpdate(int currentSeconds) {
        return currentSeconds % seconds == 0;
    }

}
