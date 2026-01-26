package com.peatroxd.congratsinator.service.impl;

import com.peatroxd.congratsinator.service.PhotoService;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    @PostConstruct
    public void init() throws Exception {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }
    }

    public String uploadPhoto(MultipartFile file, UUID personId) throws Exception {
        String key = "persons/" + personId + "/" + file.getOriginalFilename();

        log.debug("Uploading file {} to bucket {}", file.getOriginalFilename(), bucket);

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(key)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );

        log.debug("Uploaded file {} to bucket {}", file.getOriginalFilename(), bucket);

        return key;
    }

    public byte[] downloadPhoto(String fileName) throws Exception {
        try (InputStream is = minioClient.getObject(
                GetObjectArgs.builder().bucket(bucket).object(fileName).build())) {
            return is.readAllBytes();
        }
    }

    public String getPhotoUrl(String key) throws Exception {
        String url = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .bucket(bucket)
                        .object(key)
                        .method(Method.GET)
                        .expiry(1, TimeUnit.HOURS)
                        .build()
        );

        return url.replace("minio:9000", "localhost:9000");
    }
}

