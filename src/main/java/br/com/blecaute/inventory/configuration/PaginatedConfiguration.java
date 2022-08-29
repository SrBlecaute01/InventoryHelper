package br.com.blecaute.inventory.configuration;

import br.com.blecaute.inventory.button.Button;
import br.com.blecaute.inventory.validator.SlotInvalidator;
import lombok.*;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * The PaginatedConfiguration is designed to configure paginated inventories.
 */
@Getter
@Builder(toBuilder = true)
@RequiredArgsConstructor
@AllArgsConstructor
public class PaginatedConfiguration implements Configuration {

    @NonNull
    private final String identifier;

    @Builder.Default @Nullable
    private SlotInvalidator validator = null;

    private int start;
    private int end;
    private int size;

    @NonNull @Singular
    private Set<Button> buttons = new HashSet<>();

    /**
     * Create a new PaginatedConfiguration with the given identifier and SlotValidator.
     *
     * @param identifier The identifier of PaginatedConfiguration.
     * @param validator The SlotValidator to check slots of inventory.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
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
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public PaginatedConfiguration(@NonNull String identifier, int start, int end, int size) {
        this.identifier = identifier;
        this.start = start;
        this.end = end;
        this.size = size;
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

    public static class PaginatedConfigurationBuilder {

        protected PaginatedConfigurationBuilder identifier(String identifier) {
            this.identifier = identifier;
            return this;
        }

    }

}
