package com.ksr.krc_pos_backend.security;

import com.ksr.krc_pos_backend.model.RefreshToken;
import com.ksr.krc_pos_backend.model.User;
import com.ksr.krc_pos_backend.repo.RefreshTokenRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepo refreshTokenRepo;

    @Transactional
    public RefreshToken generateRefreshToken(User user) {
        RefreshToken existingToken = refreshTokenRepo.findByUser(user).orElse(null);

        if (existingToken != null) {
            existingToken.setToken(UUID.randomUUID().toString());
            existingToken.setExpiresAt(LocalDateTime.now().plusDays(7));
            existingToken.setRevoked(false);
            return refreshTokenRepo.save(existingToken);
        } else {
            RefreshToken refreshToken = RefreshToken.builder()
                    .token(UUID.randomUUID().toString())
                    .user(user)
                    .expiresAt(LocalDateTime.now().plusDays(7))
                    .revoked(false)
                    .build();

            return refreshTokenRepo.save(refreshToken);
        }
    }

    public RefreshToken validateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        if (refreshToken.isRevoked() || refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expired or revoked");
        }

        return refreshToken;
    }
}
