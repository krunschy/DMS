package at.technikum.swkom.dms.minio;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class MinioService {

    private final MinioClient minioClient;
    private static final String BUCKET_NAME = "pdf-bucket"; // Adjust as needed

    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public byte[] downloadFile(String fileName) {
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder().bucket(BUCKET_NAME).object(fileName).build())) {
            return stream.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException("Failed to download file: " + fileName, e);
        }
    }

    public String uploadFile(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            String uniqueFileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(uniqueFileName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            return "http://localhost:9000/" + BUCKET_NAME + "/" + uniqueFileName;

        } catch (IOException | MinioException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException("File upload failed: " + e.getMessage(), e);
        }
    }

    public void deleteFile(String fileUrl) {
        try {
            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);

            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(fileName)
                            .build()
            );

            System.out.println("Deleted file from MinIO: " + fileName);
        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException("File deletion failed: " + e.getMessage(), e);
        }
    }
}

