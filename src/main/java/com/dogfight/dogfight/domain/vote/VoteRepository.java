package com.dogfight.dogfight.domain.vote;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote,Long> {
    @Modifying
    @Query("UPDATE vote e SET e.option1Count = e.option1Count + 1 WHERE e.id = :id")
    void incrementFirstOptionCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE vote e SET e.option2Count = e.option2Count + 1 WHERE e.id = :id")
    void incrementSecondOptionCount(@Param("id") Long id);
}
