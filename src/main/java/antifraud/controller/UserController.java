package antifraud.controller;

import antifraud.mapper.AntiFraudMapper;
import antifraud.model.request.AccessChanging;
import antifraud.model.request.NewUser;
import antifraud.model.request.RoleChanging;
import antifraud.model.response.AccessStatus;
import antifraud.model.response.CreatedUser;
import antifraud.model.response.DeletedUser;
import antifraud.model.response.UserProfile;
import antifraud.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class UserController {
    UserService service;
    AntiFraudMapper mapper;

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    CreatedUser register(@Valid @RequestBody NewUser newUser) {
        return service
                .register(
                        newUser.name(),
                        newUser.username(),
                        newUser.password()
                );
    }

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    List<UserProfile> getUserList() {
        return service.getUserList();
    }

    @DeleteMapping("/user/{username}")
    @ResponseStatus(HttpStatus.OK)
    DeletedUser deleteUser(@Valid @PathVariable String username) {
        return service.deleteUser(username);
    }

    @PutMapping("/role")
    @ResponseStatus(HttpStatus.OK)
    UserProfile changeUserRole(@Valid @RequestBody RoleChanging roleChanging) {
        return service.changeUserRole(roleChanging.username(), roleChanging.role());
    }

    @PutMapping("/access")
    @ResponseStatus(HttpStatus.OK)
    AccessStatus changeUserAccess(@Valid @RequestBody AccessChanging accessChanging) {
        return service.changeUserAccess(accessChanging.username(), accessChanging.operation());
    }
}
