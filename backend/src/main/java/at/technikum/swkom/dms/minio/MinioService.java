package at.technikum.swkom.dms.minio;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;

    private static final String BUCKET_NAME = "pdf-bucket"; // Change as needed

    public String uploadFile(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            String uniqueFileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

            // Upload file to MinIO
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(uniqueFileName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            // Return the file URL (Assuming MinIO is running on localhost)
            return "http://localhost:9000/" + BUCKET_NAME + "/" + uniqueFileName;

        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException("MinIO file upload failed: " + e.getMessage());
        }
    }
}
