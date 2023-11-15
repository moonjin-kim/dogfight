package com.dogfight.dogfight.domain.user;

import com.dogfight.dogfight.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    /**
     * 비밀번호 확인
     * @param plainPassword 암호화 이전의 비밀번호
     * @param passwordEncoder 암호화 도구
     * @return boolean
     */
    public boolean checkPassword(String plainPassword, PasswordEncoder passwordEncoder){
        return passwordEncoder.matches(plainPassword, this.password);
    }


    public User hashPassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
        return this;
    }
}
