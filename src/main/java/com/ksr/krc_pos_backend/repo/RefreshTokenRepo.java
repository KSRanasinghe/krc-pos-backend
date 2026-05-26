package com.ksr.krc_pos_backend.repo;

import com.ksr.krc_pos_backend.model.RefreshToken;
import com.ksr.krc_pos_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);
}
