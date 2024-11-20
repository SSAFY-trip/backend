package com.ssafy.enjoytrip.global.util;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.S3Exception;

import com.ssafy.enjoytrip.global.exception.*;

@Component
@RequiredArgsConstructor
public class S3UploadUtil {
    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    public String bucketName;

    public void uploadFile(MultipartFile file, String filePath) throws IOException {
        try{
            if (file.isEmpty()) {
                throw new InvalidDataException(GlobalErrorCode.VALIDATION_FAILED);
            }
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(filePath)
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );
        } catch (IOException e) {
            throw new InvalidDataException(GlobalErrorCode.VALIDATION_FAILED);
        } catch(SdkException e) {
            throw new ExternalServerUnexpectedErrorException(GlobalErrorCode.S3_UNEXPECTED_ERROR);
        }
    }

    public void deleteFile(String filePath) {
        try {
            s3Client.deleteObject(
                    DeleteObjectRequest.builder()
                            .bucket(bucketName)
                            .key(filePath)
                            .build()
            );
        } catch(S3Exception e) {
            if (e.statusCode() == 404) {
                throw new ExternalServerNotFoundException(GlobalErrorCode.S3_FILE_NOT_FOUND);
            }
            throw new ExternalServerBadRequestException(GlobalErrorCode.S3_OPERATION_FAILED);
        } catch(SdkException e) {
            throw new ExternalServerUnexpectedErrorException(GlobalErrorCode.S3_UNEXPECTED_ERROR);
        }
    }
}