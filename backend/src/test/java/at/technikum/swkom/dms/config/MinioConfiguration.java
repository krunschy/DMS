package at.technikum.swkom.dms.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfiguration {

    @Bean
    public MinioClient minioClient(
            @Value("${minio.url}") String minioUrl,
            @Value("${minio.rootUser}") String rootUser,
            @Value("${minio.rootPassword}") String rootPassword) {
        return MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(rootUser, rootPassword)
                .build();
    }
}
