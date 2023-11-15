package com.dogfight.dogfight.domain.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByAccountAndPassword(String account, String password);
    User findByNickname(String nickname);
    User findByAccount(String account);

    @Query("select count(user.account) > 0 from User user where user.account = :account")
    boolean accountExists(@Param(value = "account") String account);
}
