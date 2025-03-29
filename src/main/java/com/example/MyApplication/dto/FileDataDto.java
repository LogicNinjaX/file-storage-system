package com.example.MyApplication.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public class FileDataDto {

    @JsonIgnore
    private int id;

    private String name;

    private long fileSize;

    private String contentType;

    private LocalDateTime uploadedAt;

    @JsonIgnore
    private UserProfileDto userProfileDto;

    public FileDataDto() {
    }

    public FileDataDto(String name, long fileSize, String contentType) {
        this.name = name;
        this.fileSize = fileSize;
        this.contentType = contentType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public UserProfileDto getUserProfileDto() {
        return userProfileDto;
    }

    public void setUserProfileDto(UserProfileDto userProfileDto) {
        this.userProfileDto = userProfileDto;
    }
}
