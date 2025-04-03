package com.example.MyApplication.service;

import com.example.MyApplication.config.AuthService;
import com.example.MyApplication.dto.ApiResponse;
import com.example.MyApplication.dto.FileDataDto;
import com.example.MyApplication.entity.FileData;
import com.example.MyApplication.entity.UserProfile;
import com.example.MyApplication.exception.FileNotFoundException;
import com.example.MyApplication.repository.FileDataRepository;
import com.example.MyApplication.repository.UserProfileRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service
public class FileDataService {

    private Logger logger  = LoggerFactory.getLogger(FileDataService.class);

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

        logger.info("file info saved in db");

        return new ApiResponse(true, response, fileDataDto);
    }


    public String deleteFile(String fileName) {

        UserProfile userProfile = authService.getLoggedInUser();

        FileData fileData = fileDataRepository.getFile(fileName, userProfile.getUsername())
                .orElseThrow(() -> new FileNotFoundException("file not found"));

        String result = s3Service.deleteFileFromFolder(userProfile.getUsername(), fileData.getName());
        fileDataRepository.delete(fileData);

        logger.info("file info deleted in db");

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