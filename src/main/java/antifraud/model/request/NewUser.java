package antifraud.model.request;

import jakarta.validation.constraints.NotBlank;

public record NewUser(
        @NotBlank String name,
        @NotBlank String username,
        @NotBlank String password
) {
}
