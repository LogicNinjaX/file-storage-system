package com.example.MyApplication.repository;


import com.example.MyApplication.entity.FileData;
import com.example.MyApplication.exception.FileNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FileDataRepository extends JpaRepository<FileData, Integer> {

    @Query("select f from FileData f where f.name = :name and f.userProfile.username = :username")
    Optional<FileData> getFile(String name, String username) throws FileNotFoundException;

}
