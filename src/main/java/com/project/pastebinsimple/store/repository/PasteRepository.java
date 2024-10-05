package com.project.pastebinsimple.store.repository;

import com.project.pastebinsimple.store.model.Paste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.time.Instant;
import java.util.List;

@Repository
public interface PasteRepository extends JpaRepository<Paste, Long> {

    boolean existsByUrl(String url);

    @Modifying
    @Query("UPDATE Paste p SET p.expiresAt = :expiration WHERE p.id = :id")
    void updateExpiration(@Param("expiration") Instant expiration, @Param("id") Long id);

    @Query("SELECT p FROM Paste p JOIN FETCH p.user WHERE p.user.oauth2Id = :oauth2Id")
    List<Paste> findByUserOauth2Id(@Param("oauth2Id") String oauth2Id);

    List<Paste> findByExpiresAtBefore(Instant expiresAt);

}
