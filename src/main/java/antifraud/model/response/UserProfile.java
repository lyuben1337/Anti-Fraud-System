package antifraud.model.response;

public record UserProfile(
        Long id,
        String name,
        String username,
        String role) {
}
