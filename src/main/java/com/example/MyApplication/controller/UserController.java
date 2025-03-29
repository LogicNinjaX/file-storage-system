package com.example.MyApplication.controller;

import com.example.MyApplication.dto.ApiResponse;
import com.example.MyApplication.dto.LoginRequest;
import com.example.MyApplication.dto.UserProfileDto;
import com.example.MyApplication.entity.UserProfile;
import com.example.MyApplication.security.JwtUtil;
import com.example.MyApplication.service.UserProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class UserController {


    private final UserProfileService userProfileService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UserController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserProfileService userProfileService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userProfileService = userProfileService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> saveUser(@RequestBody Map<String, String> userData) {

        String username = userData.get("username");
        String password = userData.get("password");

        UserProfileDto userProfileDto = userProfileService.saveUser(new UserProfile(username, password));

        ApiResponse apiResponse = new ApiResponse(
                true,
                "user created",
                userProfileDto);

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        String token = jwtUtil.generateToken(loginRequest.getUsername());
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }
}
