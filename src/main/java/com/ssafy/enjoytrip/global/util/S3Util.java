package com.ssafy.enjoytrip.global.util;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import com.ssafy.enjoytrip.global.exception.*;

@Component
@RequiredArgsConstructor
public class S3Util {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.bucket-name}")
    public String bucketName;

    public void uploadFile(MultipartFile file, String filePath) {
        try {
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
        }
        catch(SdkException e) {
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
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                throw new ExternalServerNotFoundException(GlobalErrorCode.S3_FILE_NOT_FOUND);
            }
            throw new ExternalServerBadRequestException(GlobalErrorCode.S3_OPERATION_FAILED);
        } catch (SdkException e) {
            throw new ExternalServerUnexpectedErrorException(GlobalErrorCode.S3_UNEXPECTED_ERROR);
        }
    }

    public void deleteDirectory(String pathPrefix) {
        try {
            ListObjectsV2Response response = s3Client.listObjectsV2(ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix(pathPrefix)
                    .build());

            response.contents().forEach(object ->
                    s3Client.deleteObject(DeleteObjectRequest.builder()
                            .bucket(bucketName)
                            .key(object.key())
                            .build())
            );
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                throw new ExternalServerNotFoundException(GlobalErrorCode.S3_FILE_NOT_FOUND);
            }
            throw new ExternalServerBadRequestException(GlobalErrorCode.S3_OPERATION_FAILED);
        } catch (SdkException e) {
            throw new ExternalServerUnexpectedErrorException(GlobalErrorCode.S3_UNEXPECTED_ERROR);
        }
    }

    public boolean checkIfFileExists(String filePath) {
        try {
            HeadObjectRequest request = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filePath)
                    .build();

            HeadObjectResponse response = s3Client.headObject(request);
            return true;
        }
        catch (S3Exception e) {
            if (e.awsErrorDetails().errorCode().equals("NoSuchKey")) {
                return false;
            }
            else {
                throw new ExternalServerUnexpectedErrorException(GlobalErrorCode.S3_UNEXPECTED_ERROR);
            }
        }
    }

    public URL generatePresignedUrl(String filePath) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(filePath)
                .build();

        PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(p -> p
                .getObjectRequest(getObjectRequest)
                .signatureDuration(Duration.ofMinutes(5)));

        return presignedGetObjectRequest.url();
    }


}