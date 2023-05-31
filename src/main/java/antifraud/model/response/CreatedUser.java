package antifraud.model.response;

public record CreatedUser(
        Long id,
        String name,
        String username,
        String role
) {
}
