package br.com.blecaute.inventory.task;

import br.com.blecaute.inventory.InventoryHelper;

public class InventoryUpdateTask implements Runnable {

    private int seconds = 0;

    @Override
    public void run() {
        InventoryHelper.getManager().updateInventories(seconds);
        seconds = seconds > 60 ? 0 : seconds + 1;
    }

}
