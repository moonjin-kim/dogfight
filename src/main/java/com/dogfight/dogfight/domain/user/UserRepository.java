package com.dogfight.dogfight.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByAccountAndPassword(String account, String password);
    Optional<User> findByNickname(String nickname);
    Optional<User> findByAccount(String account);
}
