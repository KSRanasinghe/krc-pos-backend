package com.ksr.krc_pos_backend.controller;

import com.ksr.krc_pos_backend.dto.UserRequestDto;
import com.ksr.krc_pos_backend.dto.UserResponseDto;
import com.ksr.krc_pos_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRequestDto userRequestDto) {
        return new ResponseEntity<>(userService.register(userRequestDto), HttpStatus.CREATED);
    }
}
