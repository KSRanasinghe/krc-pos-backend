package com.ksr.krc_pos_backend.service;

import com.ksr.krc_pos_backend.dto.UserRequestDto;
import com.ksr.krc_pos_backend.dto.UserResponseDto;
import com.ksr.krc_pos_backend.model.User;
import com.ksr.krc_pos_backend.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo repo;
    private final PasswordEncoder encoder;

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
}
