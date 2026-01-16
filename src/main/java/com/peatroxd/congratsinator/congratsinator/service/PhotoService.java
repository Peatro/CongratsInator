package com.peatroxd.congratsinator.congratsinator.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface PhotoService {

    String uploadPhoto(MultipartFile file, UUID personId) throws Exception;

    byte[] downloadPhoto(String fileName) throws Exception;

    String getPhotoUrl(String key) throws Exception;
}
