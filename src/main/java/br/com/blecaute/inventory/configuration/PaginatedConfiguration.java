package br.com.blecaute.inventory.configuration;

import br.com.blecaute.inventory.validator.SlotValidator;
import lombok.*;
import org.jetbrains.annotations.Nullable;

@Builder
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class PaginatedConfiguration {

    @NonNull
    private final String identifier;

    @Builder.Default @Nullable
    private SlotValidator validator = null;

    @Builder.Default
    private int start = 0, end = 0, size = 0;

    public PaginatedConfiguration(@NonNull String identifier, @Nullable SlotValidator validator) {
        this.identifier = identifier;
        this.validator = validator;
    }

    public PaginatedConfiguration(@NonNull String identifier, int start, int end, int size) {
        this(identifier, null, start, end, size);
    }

    public static PaginatedConfigurationBuilder builder(String identifier) {
        return new PaginatedConfigurationBuilder().identifier(identifier);
    }

}
