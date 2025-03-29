package com.example.MyApplication.config;

import com.example.MyApplication.entity.UserProfile;
import com.example.MyApplication.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class AuthService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    public UserProfile getLoggedInUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userProfileRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
