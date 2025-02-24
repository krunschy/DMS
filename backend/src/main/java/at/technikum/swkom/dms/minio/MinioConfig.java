package at.technikum.swkom.dms.minio;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {
    @Value("${minio.url}")
    private String minioUrl;

    @Value("${minio.rootUser}") // Updated variable name
    private String rootUser;

    @Value("${minio.rootPassword}") // Updated variable name
    private String rootPassword;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(rootUser, rootPassword)
                .build();
    }
}
