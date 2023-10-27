package com.dogfight.dogfight.domain.user;

import com.dogfight.dogfight.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Table(name = "users")
@Entity
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String account;

    private String password;

    private String nickname;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public User(Long id, String account, String password, String nickname, String email, Role role) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
    }
}
