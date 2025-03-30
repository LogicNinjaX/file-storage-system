package com.example.MyApplication.service;

import com.example.MyApplication.exception.FolderCreationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;

@Service
public class S3Service {

    Logger logger = LoggerFactory.getLogger(S3Service.class);

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public S3Service(@Value("${aws.accessKey}") String accessKey,
                     @Value("${aws.secretKey}") String secretKey,
                     @Value("${aws.region}") String region) {
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }

    public void createFolder(String username) {

        try{
        String folderKey = username + "/"; // End with slash to indicate a folder

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(folderKey)
                .build();

        s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromBytes(new byte[0])); // Empty object

        logger.info("folder creation completed for user: {}", username);

        }catch (RuntimeException e){
            logger.error("failed to create folder for user: {}", username);
            throw new FolderCreationFailedException("failed to create folder");
        }
    }


    public String uploadFile(MultipartFile file, String username) throws IOException {

        try {
            String fileName = username + "/" + file.getOriginalFilename(); // Store inside folder

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));
            logger.info("file uploaded by user: username");

            return "File uploaded to folder '" + username + "' successfully: " + fileName;
        }catch (RuntimeException e){
            logger.error("file upload failed");
            throw new RuntimeException(e);
        }
    }


    public byte[] getFileFromFolder(String folderName, String fileName) {
        String fileKey = folderName + "/" + fileName; // Path inside S3

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileKey)
                .build();

        ResponseInputStream<GetObjectResponse> responseInputStream = s3Client.getObject(getObjectRequest);
        try {
            return responseInputStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Error while downloading file: " + fileKey, e);
        }
    }


    public String deleteFileFromFolder(String folderName, String fileName) {
        try {
            String fileKey = folderName + "/" + fileName; // Full path in S3

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            logger.info("file deleted successfully");
            return "File deleted successfully: " + fileKey;
        }catch (RuntimeException e){
            logger.error("file deletion failed");
            throw new RuntimeException(e);
        }
    }




    // for admin panel
    public String deleteFolder(String folderName) {

        try {
            String folderKey = folderName + "/"; // Folder path

            // List all files inside the folder
            ListObjectsV2Request listObjectsRequest = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix(folderKey) // Get all files in folder
                    .build();

            ListObjectsV2Response listObjectsResponse = s3Client.listObjectsV2(listObjectsRequest);

            // Delete all files inside the folder
            for (S3Object s3Object : listObjectsResponse.contents()) {
                DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(s3Object.key()) // Delete each file
                        .build();
                s3Client.deleteObject(deleteObjectRequest);
            }

            logger.info("folder deleted successfully");
            return "Folder deleted successfully: " + folderName;
        }catch (RuntimeException e){

            logger.error("folder deletion failed");
            throw new RuntimeException(e);
        }
    }
}
