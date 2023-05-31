package antifraud.model.entity;

import antifraud.model.consts.UserRole;
import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class UserEntity {
    @Id @GeneratedValue @NotNull
    Long id;

    @NotEmpty
    @Column(updatable = false)
    String name;

    @NotEmpty
    @Column(updatable = false)
    String username;

    @NotEmpty
    String password;

    @Enumerated(EnumType.STRING)
    UserRole role;

    Boolean locked;
}
