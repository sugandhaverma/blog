package org.scaler.my_blog.service;

import org.scaler.my_blog.payload.UploadResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

@Service
public class MediaStorageService {
    private static final Map<String, MediaFormat> ALLOWED_TYPES = Map.of(
            "image/jpeg", new MediaFormat("image", "jpg"),
            "image/png", new MediaFormat("image", "png"),
            "image/gif", new MediaFormat("image", "gif"),
            "image/webp", new MediaFormat("image", "webp"),
            "video/mp4", new MediaFormat("video", "mp4"),
            "video/webm", new MediaFormat("video", "webm"),
            "video/quicktime", new MediaFormat("video", "mov")
    );

    private final Path uploadDirectory;

    public MediaStorageService(@Value("${app.upload-dir:uploads}") String uploadDirectory) {
        this.uploadDirectory = Path.of(uploadDirectory).toAbsolutePath().normalize();
    }

    public UploadResponse store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Choose an image or video to upload.");
        }

        MediaFormat format = ALLOWED_TYPES.get(file.getContentType());
        if (format == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unsupported media type. Use JPEG, PNG, GIF, WebP, MP4, WebM, or MOV."
            );
        }

        String filename = UUID.randomUUID() + "." + format.extension();
        Path destination = uploadDirectory.resolve(filename).normalize();
        if (!destination.startsWith(uploadDirectory)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid upload path.");
        }

        try {
            Files.createDirectories(uploadDirectory);
            file.transferTo(destination);
        } catch (IOException exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to store uploaded media.", exception);
        }

        return new UploadResponse("/uploads/" + filename, format.mediaType());
    }

    private record MediaFormat(String mediaType, String extension) {
    }
}
