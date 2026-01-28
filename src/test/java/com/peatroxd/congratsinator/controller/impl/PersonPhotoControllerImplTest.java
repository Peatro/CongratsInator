package com.peatroxd.congratsinator.controller.impl;

import com.peatroxd.congratsinator.model.Person;
import com.peatroxd.congratsinator.service.PersonService;
import com.peatroxd.congratsinator.service.PhotoService;
import com.peatroxd.congratsinator.testdata.Persons;
import com.peatroxd.congratsinator.testdata.Values;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static com.peatroxd.congratsinator.controller.ApiPathsTestData.PERSON_CONTROLLER_BY_ID;
import static com.peatroxd.congratsinator.controller.ApiPathsTestData.PERSON_PHOTO_ENDPOINT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PersonPhotoControllerImpl.class)
class PersonPhotoControllerImplTest {
    private static final String MULTIPART_FIELD_FILE = "file";

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PersonService personService;

    @MockitoBean
    PhotoService photoService;

    @Test
    void uploadPhoto_uploadsAndUpdatesPerson_andReturnsUrl() throws Exception {
        UUID id = UUID.randomUUID();

        byte[] fileBytes = "JPEG_BYTES".getBytes(StandardCharsets.UTF_8);
        String key = "persons/" + id + "/avatar.jpg";

        when(photoService.uploadPhoto(any(), eq(id))).thenReturn(key);

        mockMvc.perform(multipart(PERSON_PHOTO_ENDPOINT, id)
                        .file(MULTIPART_FIELD_FILE, fileBytes)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string("/api/persons/" + id + "/photo"));

        ArgumentCaptor<org.springframework.web.multipart.MultipartFile> fileCaptor =
                ArgumentCaptor.forClass(org.springframework.web.multipart.MultipartFile.class);

        verify(photoService).uploadPhoto(fileCaptor.capture(), eq(id));
        assertThat(fileCaptor.getValue()
                .getName()).isEqualTo(MULTIPART_FIELD_FILE);

        verify(personService).updatePhotoPath(id, key);
        verifyNoMoreInteractions(photoService, personService);
    }

    @Test
    void getPhoto_downloadsPhotoByPersonsPhotoKey_andReturnsBytesAndJpegContentType() throws Exception {
        UUID id = UUID.randomUUID();

        Person person = Persons.persistedWithIdAndName(id, Values.PERSON_NAME);
        byte[] data = new byte[]{1, 2, 3, 4};

        when(personService.getById(id)).thenReturn(person);
        when(photoService.downloadPhoto(Values.PHOTO_KEY)).thenReturn(data);

        mockMvc.perform(get(PERSON_PHOTO_ENDPOINT, id))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "image/jpeg"))
                .andExpect(content().bytes(data));

        verify(personService).getById(id);
        verify(photoService).downloadPhoto(Values.PHOTO_KEY);
        verifyNoMoreInteractions(photoService, personService);
    }

    @Test
    void uploadPhoto_whenMultipartFieldIsNotFile_returns400() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(multipart(PERSON_PHOTO_ENDPOINT, id)
                        .file("not_file", "X".getBytes(StandardCharsets.UTF_8))
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(photoService, personService);
    }
}
