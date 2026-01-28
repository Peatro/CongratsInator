package com.peatroxd.congratsinator.service.impl;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PhotoServiceImplTest {

    private static final String BUCKET_NAME = "persons-photos";
    private static final String BUCKET_FIELD = "bucket";
    private static final String CONTENT_TYPE_PNG = "image/png";

    private static final String PERSONS_PREFIX = "persons/";
    private static final String FILE_NAME = "avatar.png";
    private static final String OBJECT_PATH = "persons/x/file.png";

    private static final String INTERNAL_MINIO_HOST = "minio:9000";
    private static final String PUBLIC_MINIO_HOST = "localhost:9000";
    private static final String SIGNATURE_QS = "?X-Amz-Signature=123";
    private static final String HTTP_LINK = "http://";

    @Mock
    private MinioClient minioClient;

    private PhotoServiceImpl photoService;

    @BeforeEach
    void setUp() {
        photoService = new PhotoServiceImpl(minioClient);
        ReflectionTestUtils.setField(photoService, BUCKET_FIELD, BUCKET_NAME);
    }

    @Test
    void init_whenBucketExists_doesNotCreateBucket() throws Exception {
        when(minioClient.bucketExists(any(BucketExistsArgs.class))).thenReturn(true);

        photoService.init();

        verify(minioClient).bucketExists(any(BucketExistsArgs.class));
        verify(minioClient, never()).makeBucket(any(MakeBucketArgs.class));
        verifyNoMoreInteractions(minioClient);
    }

    @Test
    void init_whenBucketNotExists_createsBucket() throws Exception {
        when(minioClient.bucketExists(any(BucketExistsArgs.class))).thenReturn(false);

        photoService.init();

        verify(minioClient).bucketExists(any(BucketExistsArgs.class));
        verify(minioClient).makeBucket(any(MakeBucketArgs.class));
        verifyNoMoreInteractions(minioClient);
    }

    @Test
    void uploadPhoto_buildsKey_andCallsPutObject() throws Exception {
        MockMultipartFile file = multipartPng();
        UUID personId = UUID.randomUUID();

        String expectedKey = expectedKeyFor(personId);

        ArgumentCaptor<PutObjectArgs> captor = ArgumentCaptor.forClass(PutObjectArgs.class);

        String actualKey = photoService.uploadPhoto(file, personId);

        assertThat(actualKey).isEqualTo(expectedKey);

        verify(minioClient).putObject(captor.capture());
        PutObjectArgs args = captor.getValue();

        assertThat(args.bucket()).isEqualTo(BUCKET_NAME);
        assertThat(args.object()).isEqualTo(expectedKey);
        assertThat(args.contentType()).isEqualTo(CONTENT_TYPE_PNG);

        verifyNoMoreInteractions(minioClient);
    }

    @Test
    void downloadPhoto_readsAllBytesFromMinioStream() throws Exception {
        byte[] expected = "HELLO".getBytes(UTF_8);

        GetObjectResponse response = mock(GetObjectResponse.class);
        when(response.readAllBytes()).thenReturn(expected);

        when(minioClient.getObject(any(GetObjectArgs.class)))
                .thenReturn(response);

        byte[] result = photoService.downloadPhoto(OBJECT_PATH);

        assertThat(result).isEqualTo(expected);

        ArgumentCaptor<GetObjectArgs> captor = ArgumentCaptor.forClass(GetObjectArgs.class);
        verify(minioClient).getObject(captor.capture());

        GetObjectArgs args = captor.getValue();
        assertThat(args.bucket()).isEqualTo(BUCKET_NAME);
        assertThat(args.object()).isEqualTo(OBJECT_PATH);

        verifyNoMoreInteractions(minioClient);
    }

    @Test
    void getPhotoUrl_replacesMinioHostForBrowserAccess_andKeepsBucketAndKey() throws Exception {
        UUID personId = UUID.randomUUID();
        String key = expectedKeyFor(personId);

        String presigned = presignedUrl(key);

        when(minioClient.getPresignedObjectUrl(any(GetPresignedObjectUrlArgs.class)))
                .thenReturn(presigned);

        String result = photoService.getPhotoUrl(key);

        assertThat(result).contains(PUBLIC_MINIO_HOST);
        assertThat(result).doesNotContain(INTERNAL_MINIO_HOST);
        assertThat(result).contains("/" + BUCKET_NAME + "/" + key);

        ArgumentCaptor<GetPresignedObjectUrlArgs> captor = ArgumentCaptor.forClass(GetPresignedObjectUrlArgs.class);
        verify(minioClient).getPresignedObjectUrl(captor.capture());

        GetPresignedObjectUrlArgs args = captor.getValue();
        assertThat(args.bucket()).isEqualTo(BUCKET_NAME);
        assertThat(args.object()).isEqualTo(key);
        assertThat(args.method()).isEqualTo(Method.GET);

        verifyNoMoreInteractions(minioClient);
    }

    private static MockMultipartFile multipartPng() {
        return new MockMultipartFile(
                "file",
                FILE_NAME,
                CONTENT_TYPE_PNG,
                "PNG_BYTES".getBytes(UTF_8)
        );
    }

    private static String expectedKeyFor(UUID personId) {
        return PERSONS_PREFIX + personId + "/" + FILE_NAME;
    }

    private static String presignedUrl(String key) {
        return HTTP_LINK + INTERNAL_MINIO_HOST + "/" + BUCKET_NAME + "/" + key + SIGNATURE_QS;
    }
}
