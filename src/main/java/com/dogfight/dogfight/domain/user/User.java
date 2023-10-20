package com.dogfight.dogfight.domain.user;

import com.dogfight.dogfight.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Table(name = "users")
@Entity
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String password;


}
