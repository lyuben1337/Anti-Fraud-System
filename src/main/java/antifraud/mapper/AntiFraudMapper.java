package antifraud.mapper;

import antifraud.model.entity.UserEntity;
import antifraud.model.response.CreatedUser;
import antifraud.model.response.UserProfile;
import org.mapstruct.Mapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface AntiFraudMapper {

    CreatedUser toCreatedUser(UserEntity user);

    UserProfile toUserProfile(UserEntity user);

    default UserDetails toUserDetails(UserEntity user) {
        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(new SimpleGrantedAuthority(user.getRole().name()))
                .accountLocked(user.getLocked())
                .build();
    }

}

