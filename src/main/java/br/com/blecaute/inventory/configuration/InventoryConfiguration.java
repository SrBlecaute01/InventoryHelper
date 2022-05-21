package br.com.blecaute.inventory.configuration;

import br.com.blecaute.inventory.exception.InventoryBuilderException;
import lombok.Builder;
import lombok.Getter;

@Builder @Getter
public class InventoryConfiguration implements Cloneable{

    private String title;
    private final int lines;

    public InventoryConfiguration(String title, int lines) {
        this.title = title;
        this.lines = lines;
    }

    public static InventoryConfigurationBuilder builder(String name, int lines) {
        return new InventoryConfigurationBuilder().title(name).lines(lines);
    }

    @Override
    public InventoryConfiguration clone() {
        try {
            return (InventoryConfiguration) super.clone();

        } catch (CloneNotSupportedException exception) {
            throw new InventoryBuilderException(exception);
        }
    }
}