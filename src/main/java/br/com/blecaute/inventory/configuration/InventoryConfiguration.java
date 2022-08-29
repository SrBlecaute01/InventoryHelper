package br.com.blecaute.inventory.configuration;

import br.com.blecaute.inventory.exception.InventoryBuilderException;
import lombok.Builder;
import lombok.Getter;

/**
 * The InventoryConfiguration is designed to configure InventoryBuilder.
 */
@Builder @Getter
public class InventoryConfiguration implements Configuration, Cloneable {

    private String title;
    private final int lines;

    /**
     * Create a new InventoryConfiguration with given title and lines.
     *
     * @param title The title of the inventory.
     * @param lines The number of lines of the inventory.
     */
    public InventoryConfiguration(String title, int lines) {
        this.title = title;
        this.lines = lines;
    }

    /**
     * Create a new InventoryConfiguration with given title and lines.
     *
     * @param name The name of the inventory.
     * @param lines The number of lines of the inventory.
     *
     * @return The InventoryConfiguration.
     */
    public static InventoryConfigurationBuilder builder(String name, int lines) {
        return new InventoryConfigurationBuilder().title(name).lines(lines);
    }

    /**
     * Clone the InventoryConfiguration.
     *
     * @return The copy of InventoryConfiguration.
     */
    @Override
    public InventoryConfiguration clone() {
        try {
            return (InventoryConfiguration) super.clone();

        } catch (CloneNotSupportedException exception) {
            throw new InventoryBuilderException(exception);
        }
    }
}