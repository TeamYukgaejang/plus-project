package org.example.plusproject.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.plusproject.common.entity.BaseEntity;
import org.example.plusproject.domain.user.enums.Role;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User extends BaseRemovableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    private User(String email, String password, String nickname, Role role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    public static User of(String email, String password, String nickname, Role role) {
        return new User(email, password, nickname, role);
    }
}
