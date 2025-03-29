package com.example.MyApplication.service;

import com.example.MyApplication.dto.UserProfileDto;
import com.example.MyApplication.entity.UserProfile;
import com.example.MyApplication.exception.UserAlreadyExistsException;
import com.example.MyApplication.repository.UserProfileRepository;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final S3Service s3Service;



    public UserProfileService(UserProfileRepository userProfileRepository, ModelMapper modelMapper, S3Service s3Service, PasswordEncoder passwordEncoder) {
        this.userProfileRepository = userProfileRepository;
        this.modelMapper = modelMapper;
        this.s3Service = s3Service;
        this.passwordEncoder = passwordEncoder;
    }

    public UserProfileDto saveUser(UserProfile userProfile){

        try{
        userProfile.setPassword(passwordEncoder.encode(userProfile.getPassword()));
        UserProfile tempVar =  userProfileRepository.save(userProfile);

        s3Service.createFolder(userProfile.getUsername());  // creating unique folder inside bucket for each user

        return modelMapper.map(tempVar, UserProfileDto.class);

        }catch (DataIntegrityViolationException e){
            throw new UserAlreadyExistsException("User Already Exists");
        }
    }






}
