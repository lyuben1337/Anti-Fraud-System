package antifraud.service;

import antifraud.model.response.AccessStatus;
import antifraud.model.response.CreatedUser;
import antifraud.model.response.DeletedUser;
import antifraud.model.response.UserProfile;

import java.util.List;

public interface UserService {

    CreatedUser register(String name, String username, String password);

    List<UserProfile> getUserList();

    DeletedUser deleteUser(String username);

    UserProfile changeUserRole(String username, String role);

    AccessStatus changeUserAccess(String username, String operation);
}
