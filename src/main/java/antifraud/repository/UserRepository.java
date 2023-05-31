package antifraud.repository;

import antifraud.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsernameIgnoreCase(String username);

    boolean existsByUsernameIgnoreCase(String name);

    default Optional<UserEntity> deleteByUsernameIgnoreCase(String username) {
        if(!existsByUsernameIgnoreCase(username)){
            return Optional.empty();
        }
        var user = findByUsernameIgnoreCase(username).get();
        delete(user);
        return Optional.of(user);
    }
}