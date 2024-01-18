import br.com.blecaute.inventory.property.InventoryProperty;
import br.com.blecaute.inventory.property.type.WeakInventoryProperty;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class A {

    private static InventoryProperty property = new WeakInventoryProperty();

    public static void main(String[] args) throws InterruptedException {
        A a = new A();
        a.a();

        a = null;

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            System.out.println(property.getOrNull("a", String.class));
            System.gc();
        }, 0, 10, TimeUnit.SECONDS);
    }

    public void a() throws InterruptedException {
        String test = "This is a test";
        property.set("a", test);

        System.out.println(property.getOrNull("a", String.class));

        test = null;
        System.gc();
    }

}
