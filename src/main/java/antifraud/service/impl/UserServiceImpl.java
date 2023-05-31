package antifraud.service.impl;

import antifraud.mapper.AntiFraudMapper;
import antifraud.model.consts.UserAccess;
import antifraud.model.consts.UserRole;
import antifraud.model.entity.UserEntity;
import antifraud.model.response.AccessStatus;
import antifraud.model.response.CreatedUser;
import antifraud.model.response.DeletedUser;
import antifraud.model.response.UserProfile;
import antifraud.repository.UserRepository;
import antifraud.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    UserRepository repository;
    PasswordEncoder passwordEncoder;
    AntiFraudMapper mapper;

    @Override
    public CreatedUser register(String name, String username, String password) {
        if (!repository.existsByUsernameIgnoreCase(username))
            return mapper.toCreatedUser(
                    repository.save(UserEntity.builder()
                            .name(name)
                            .username(username)
                            .password(passwordEncoder.encode(password))
                            .role(repository.count() == 0 ?
                                    UserRole.ADMINISTRATOR : UserRole.MERCHANT)
                            .locked(repository.count() != 0)
                            .build()
                    )
            );
        else
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Username '" + username + "' is already occupied");
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserProfile> getUserList() {
        return repository.findAll()
                .stream()
                .map(mapper::toUserProfile)
                .collect(Collectors.toList());
    }

    @Override
    public DeletedUser deleteUser(String username) {
        if(!repository.existsByUsernameIgnoreCase(username)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    username + " is not found");
        }
        return new DeletedUser (
                repository.deleteByUsernameIgnoreCase(username).orElseThrow().getUsername(),
                "Deleted successfully!"
        );
    }

    @Override
    public UserProfile changeUserRole(String username, String role) {
        var user = repository.findByUsernameIgnoreCase(username).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        username + " is not found")
        );
        roleValidation(role, user);
        user.setRole(UserRole.valueOf(role));
        repository.save(user);
        return mapper.toUserProfile(user);
    }

    @Override
    public AccessStatus changeUserAccess(String username, String operation) {
        var user = repository.findByUsernameIgnoreCase(username).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        username + " is not found")
        );
        accessValidation(operation, user);
        user.setLocked(UserAccess.valueOf(operation).equals(UserAccess.LOCK));
        repository.save(user);
        return new AccessStatus("User " + username + " " +
                (operation.equals(UserAccess.UNLOCK.name()) ?
                        "unlocked!" : "locked!"));
    }

    private void roleValidation(String role, UserEntity user) {
        if(UserRole.ADMINISTRATOR.name().equals(role)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Administrator role can't be changed");
        }
        if(!UserRole.MERCHANT.name().equals(role)
                && !UserRole.SUPPORT.name().equals(role)
        ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only MERCHANT or SUPPORT role can be given!");
        }
        if(user.getRole().equals(UserRole.valueOf(role))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    role + " has already this role");
        }
    }

    private void accessValidation(String operation, UserEntity user) {
        if(!UserAccess.LOCK.name().equals(operation)
                && !UserAccess.UNLOCK.name().equals(operation)
        ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only LOCK/UNLOCK user can be used");
        }
        if(UserRole.ADMINISTRATOR.equals(user.getRole())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Administrator status can't be changed");
        }
    }
}
