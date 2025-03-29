package com.example.MyApplication.security;

import com.example.MyApplication.entity.UserProfile;
import com.example.MyApplication.exception.UserNotFoundException;
import com.example.MyApplication.repository.UserProfileRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserProfileRepository userProfileRepository;


    public UserDetailsServiceImpl(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserProfile userProfile = userProfileRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("user not found"));


        return User.builder()
                .username(userProfile.getUsername())
                .password(userProfile.getPassword())
                .roles(userProfile.getUserRoles().toArray(new String[0]))
                .build();
    }
}
