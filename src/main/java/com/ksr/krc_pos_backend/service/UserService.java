package com.ksr.krc_pos_backend.service;

import com.ksr.krc_pos_backend.dto.AuthResponseDto;
import com.ksr.krc_pos_backend.dto.UserRequestDto;
import com.ksr.krc_pos_backend.dto.UserResponseDto;
import com.ksr.krc_pos_backend.model.RefreshToken;
import com.ksr.krc_pos_backend.model.User;
import com.ksr.krc_pos_backend.repo.UserRepo;
import com.ksr.krc_pos_backend.security.AppUserDetails;
import com.ksr.krc_pos_backend.security.JwtService;
import com.ksr.krc_pos_backend.security.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo repo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    public UserResponseDto register(@RequestBody UserRequestDto userRequestDto) {
        User user = User.builder()
                .username(userRequestDto.getUsername())
                .password(encoder.encode(userRequestDto.getPassword()))
                .build();

        User savedUser = repo.save(user);

        return UserResponseDto.builder()
                .uuid(savedUser.getUuid())
                .username(savedUser.getUsername())
                .createdAt(savedUser.getCreatedAt())
                .build();
    }

    public AuthResponseDto login(UserRequestDto userRequestDto) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequestDto.getUsername(), userRequestDto.getPassword())
        );

        User user = repo.findByUsername(userRequestDto.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));

        String accessToken = jwtService.generateToken(new AppUserDetails(user));
        RefreshToken refreshToken = refreshTokenService.generateRefreshToken(user);

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .user(
                        UserResponseDto.builder()
                                .uuid(user.getUuid())
                                .username(user.getUsername())
                                .createdAt(user.getCreatedAt())
                                .build()
                )
                .build();
    }

    public AuthResponseDto validateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenService.validateRefreshToken(token);

        User user = refreshToken.getUser();

        String newAccessToken = jwtService.generateToken(new AppUserDetails(user));
        RefreshToken newRefreshToken = refreshTokenService.generateRefreshToken(user);

        return AuthResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .user(
                        UserResponseDto.builder()
                                .uuid(user.getUuid())
                                .username(user.getUsername())
                                .createdAt(user.getCreatedAt())
                                .build()
                )
                .build();
    }
}
