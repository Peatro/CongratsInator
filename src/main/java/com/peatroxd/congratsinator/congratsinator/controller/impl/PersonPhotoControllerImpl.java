package com.peatroxd.congratsinator.congratsinator.controller.impl;

import com.peatroxd.congratsinator.congratsinator.model.Person;
import com.peatroxd.congratsinator.congratsinator.service.PersonService;
import com.peatroxd.congratsinator.congratsinator.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/persons/{id}/photo")
@RequiredArgsConstructor
public class PersonPhotoControllerImpl {

    private final PersonService personService;
    private final PhotoService photoService;

    @PostMapping
    public ResponseEntity<String> uploadPhoto(@PathVariable UUID id,
                                              @RequestParam("file") MultipartFile file) throws Exception {

        String key = photoService.uploadPhoto(file, id);       // Загружаем в MinIO
        personService.updatePhotoPath(id, key);               // Сохраняем ключ в БД

        // Возвращаем URL через наш Spring Boot, а не напрямую в MinIO
        String url = "/api/persons/" + id + "/photo";
        return ResponseEntity.ok(url);
    }

    @GetMapping
    public ResponseEntity<byte[]> getPhoto(@PathVariable UUID id) throws Exception {
        Person person = personService.getById(id);
        byte[] data = photoService.downloadPhoto(person.getPhotoKey());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(data);
    }
}

