package br.com.blecaute.inventory.configuration;

import br.com.blecaute.inventory.validator.SlotInvalidator;
import lombok.*;
import org.jetbrains.annotations.Nullable;

/**
 * The PaginatedConfiguration is designed to configure paginated inventories.
 */
@Builder @Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class PaginatedConfiguration {

    @NonNull
    private final String identifier;

    @Builder.Default @Nullable
    private SlotInvalidator validator = null;

    @Builder.Default
    private int start = 0, end = 0, size = 0;

    /**
     * Create a new PaginatedConfiguration with the given identifier and SlotValidator.
     *
     * @param identifier The identifier of PaginatedConfiguration.
     * @param validator The SlotValidator to check slots of inventory.
     */
    public PaginatedConfiguration(@NonNull String identifier, @Nullable SlotInvalidator validator) {
        this.identifier = identifier;
        this.validator = validator;
    }

    /**
     * Create a new PaginatedConfiguration with the given identifier,
     * start slot, end slot and size of objects in each page.
     *
     * @param identifier The identifier of PaginatedConfiguration.
     * @param start The slot to start place of item in inventory.
     * @param end The slot to stop place of item in inventory.
     * @param size The size of objects in each page.
     */
    public PaginatedConfiguration(@NonNull String identifier, int start, int end, int size) {
        this(identifier, null, start, end, size);
    }

    /**
     * Create a new PaginatedConfigurationBuilder with the given identifier.
     *
     * @param identifier The identifier of PaginatedConfiguration.
     *
     * @return The PaginatedConfigurationBuilder.
     */
    public static PaginatedConfigurationBuilder builder(String identifier) {
        return new PaginatedConfigurationBuilder().identifier(identifier);
    }

}
