package com.ksr.krc_pos_backend.controller;

import com.ksr.krc_pos_backend.dto.AuthResponseDto;
import com.ksr.krc_pos_backend.dto.UserRequestDto;
import com.ksr.krc_pos_backend.dto.UserResponseDto;
import com.ksr.krc_pos_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRequestDto userRequestDto) {
        return new ResponseEntity<>(userService.register(userRequestDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody UserRequestDto userRequestDto) {
        return new ResponseEntity<>(userService.login(userRequestDto), HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> validateRefreshToken(@RequestParam String refreshToken) {
        return new ResponseEntity<>(userService.validateRefreshToken(refreshToken), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestParam String refreshToken) {
        userService.logout(refreshToken);
        return ResponseEntity.noContent().build();
    }
}
