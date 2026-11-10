package org.scaler.my_blog.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MediaStorageServiceTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void storesAllowedImageWithGeneratedFilename() throws Exception {
        MediaStorageService storageService = new MediaStorageService(temporaryDirectory.toString());
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "my-photo.png",
                "image/png",
                new byte[]{1, 2, 3}
        );

        var response = storageService.store(file);

        assertEquals("image", response.mediaType());
        assertTrue(response.url().matches("/uploads/[0-9a-f-]{36}\\.png"));
        assertTrue(Files.exists(temporaryDirectory.resolve(response.url().substring("/uploads/".length()))));
    }

    @Test
    void rejectsUnsupportedMediaTypes() {
        MediaStorageService storageService = new MediaStorageService(temporaryDirectory.toString());
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "notes.txt",
                "text/plain",
                "not media".getBytes()
        );

        assertThrows(ResponseStatusException.class, () -> storageService.store(file));
    }

    @Test
    void rejectsEmptyFiles() {
        MediaStorageService storageService = new MediaStorageService(temporaryDirectory.toString());
        MockMultipartFile file = new MockMultipartFile("file", "empty.png", "image/png", new byte[0]);

        assertThrows(ResponseStatusException.class, () -> storageService.store(file));
    }
}
