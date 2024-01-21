package com.dogfight.dogfight.domain.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag,Long>, TagRepositoryCustom {

    Optional<Tag> findByName(String name);
}
