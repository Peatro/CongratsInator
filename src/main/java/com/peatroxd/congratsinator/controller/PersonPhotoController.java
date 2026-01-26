package com.peatroxd.congratsinator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface PersonPhotoController {

    ResponseEntity<String> uploadPhoto(Long id, MultipartFile file);

    ResponseEntity<byte[]> getPhoto(Long id);
}
