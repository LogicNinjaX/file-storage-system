package com.example.MyApplication.controller;


import com.example.MyApplication.dto.ApiResponse;
import com.example.MyApplication.service.FileDataService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/s3")
public class FileController {

    private FileDataService fileDataService;

    public FileController(FileDataService fileDataService) {
        this.fileDataService = fileDataService;
    }


    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {

        ApiResponse apiResponse = fileDataService.uploadFile(file);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }


    @DeleteMapping("/delete/{file-name}")
    public ResponseEntity<ApiResponse> deleteFile(@PathVariable("file-name") String fileName){
        String result = fileDataService.deleteFile(fileName);

        ApiResponse apiResponse = new ApiResponse(true, result, "{}");

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }



    /*
    @GetMapping("/files")
    public ResponseEntity<ApiResponse> getFiles(@RequestParam int pageNumber, @RequestParam int pageSize){

        ApiResponse apiResponse = new ApiResponse(true,
                "list of files from page no:"+pageNumber,
                fileDataService.getFiles(PageRequest.of(pageNumber-1, pageSize)));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

     */



    @GetMapping("/files")
    public ResponseEntity<ApiResponse> getFiles(@RequestParam int pageNumber, @RequestParam int pageSize, @RequestParam(defaultValue = "name") String sortBy, @RequestParam(defaultValue = "asc") String dir){

        ApiResponse apiResponse = new ApiResponse(true,
                "list of files from page no:"+pageNumber+", sorted by "+sortBy+", direction "+dir,
                fileDataService.getFiles(pageNumber-1, pageSize, sortBy, dir));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


}
