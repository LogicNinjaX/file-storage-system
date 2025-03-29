package com.example.MyApplication.service;

import com.example.MyApplication.config.AuthService;
import com.example.MyApplication.dto.ApiResponse;
import com.example.MyApplication.dto.FileDataDto;
import com.example.MyApplication.entity.FileData;
import com.example.MyApplication.entity.UserProfile;
import com.example.MyApplication.exception.FileNotFoundException;
import com.example.MyApplication.exception.UserNotFoundException;
import com.example.MyApplication.repository.FileDataRepository;
import com.example.MyApplication.repository.UserProfileRepository;
import com.example.MyApplication.security.JwtUtil;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;


@Service
public class FileDataService {

    private final FileDataRepository fileDataRepository;
    private final ModelMapper modelMapper;
    private final S3Service s3Service;
    private final AuthService authService;

    public FileDataService(FileDataRepository fileDataRepository, UserProfileRepository userProfileRepository, ModelMapper modelMapper, S3Service s3Service, AuthService authService) {
        this.fileDataRepository = fileDataRepository;
        this.modelMapper = modelMapper;
        this.authService = authService;
        this.s3Service = s3Service;
    }

    @Transactional
    public ApiResponse uploadFile(MultipartFile file) throws IOException {

        UserProfile userProfile = authService.getLoggedInUser();

        String response = s3Service.uploadFile(file, userProfile.getUsername());

        FileData fileData = new FileData(file.getOriginalFilename(), file.getSize(), file.getContentType()); // file related data to store in database

        fileData.setUserProfile(userProfile);

        FileDataDto fileDataDto = modelMapper.map(fileDataRepository.save(fileData), FileDataDto.class); // converting entity to dto

        return new ApiResponse(true, response, fileDataDto);
    }


    public String deleteFile(String fileName) {

        UserProfile userProfile = authService.getLoggedInUser();

        FileData fileData = fileDataRepository.getFile(fileName, userProfile.getUsername())
                .orElseThrow(() -> new FileNotFoundException("file not found"));

        String result = s3Service.deleteFileFromFolder(userProfile.getUsername(), fileData.getName());
        fileDataRepository.delete(fileData);

        return result;
    }


    public List<FileDataDto> getFiles(Pageable pageable){
        return fileDataRepository.findAll(pageable).stream().map(file -> modelMapper.map(file, FileDataDto.class)).toList();
    }

    public List<FileDataDto> getFiles(int pageNumber, int pageSize, String sortBy, String dir){

        Sort sort = dir.equalsIgnoreCase(Sort.Direction.ASC.name())?
                Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();

        return fileDataRepository.findAll(PageRequest.of(pageNumber, pageSize, sort))
                .stream().map(file -> modelMapper.map(file, FileDataDto.class)).toList();
    }


}