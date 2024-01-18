package br.com.blecaute.inventory.configuration;

import br.com.blecaute.inventory.exception.InventoryBuilderException;
import br.com.blecaute.inventory.property.InventoryProperty;
import br.com.blecaute.inventory.property.type.HashInventoryProperty;
import com.google.common.base.Preconditions;
import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * The InventoryConfiguration is designed to configure InventoryBuilder.
 */
@Builder @Getter
public class InventoryConfiguration implements Configuration, Cloneable {

    private String title;
    private final int lines;

    @Builder.Default
    private InventoryProperty properties = new HashInventoryProperty();

    /**
     * Create a new InventoryConfiguration with given title and lines.
     *
     * @param title The inventory title.
     * @param lines The number of inventory lines.
     */
    public InventoryConfiguration(@NotNull String title, int lines) {
        this.title = Preconditions.checkNotNull(title, "title cannot be null");
        this.lines = lines;
    }

    /**
     * Create a new InventoryConfiguration with given title, lines and properties.
     *
     * @param title The inventory title.
     * @param lines The number of inventory lines.
     * @param properties The inventory properties.
     */
    public InventoryConfiguration(@NotNull String title, int lines, @NotNull InventoryProperty properties) {
        this.title = Preconditions.checkNotNull(title, "title cannot be null");
        this.properties = Preconditions.checkNotNull(properties, "properties cannot be null");
        this.lines = lines;
    }

    /**
     * Set the title of the inventory.
     *
     * @param title The inventory title.
     */
    public void setTitle(@NotNull String title) {
        this.title = Preconditions.checkNotNull(title, "title cannot be null");
    }

    /**
     * Set the properties of the inventory.
     *
     * @param properties The inventory properties.
     */
    public void setProperties(@NotNull InventoryProperty properties) {
        this.properties = Preconditions.checkNotNull(properties, "properties cannot be null");
    }

    /**
     * Create a new InventoryConfiguration with given title and lines.
     *
     * @param title The inventory title
     * @param lines The number of inventory lines
     *
     * @return The InventoryConfiguration.
     */
    public static InventoryConfigurationBuilder builder(@NotNull String title, int lines) {
        Preconditions.checkNotNull(title, "title cannot be null");
        return new InventoryConfigurationBuilder().title(title).lines(lines);
    }

    /**
     * Clone the InventoryConfiguration.
     *
     * @return The copy of InventoryConfiguration.
     */
    @Override
    public InventoryConfiguration clone() {
        try {
            InventoryConfiguration configuration = (InventoryConfiguration) super.clone();
            configuration.properties = this.properties.deepClone();

            return configuration;

        } catch (CloneNotSupportedException exception) {
            throw new InventoryBuilderException(exception);
        }
    }
}