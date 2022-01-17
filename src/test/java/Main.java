import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.type.InventoryItem;

public class Main {

    public static void main(String[] args) {
        new InventoryBuilder<>("a", 6)
                .withItem(10, null, click -> {
                    InventoryItem type = click.getObject();

                });
    }

}
