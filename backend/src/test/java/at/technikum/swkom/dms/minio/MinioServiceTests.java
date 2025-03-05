package at.technikum.swkom.dms.minio;

import io.minio.MinioClient;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MinioServiceTests {

    @Mock
    private MinioClient minioClient;

    private MinioService minioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        minioService = new MinioService(minioClient);
        ReflectionTestUtils.setField(minioService, "minioClient", minioClient);
    }

    @Test
    void testUploadFileSuccess() throws Exception {
        byte[] content = "file content".getBytes();
        MockMultipartFile file = new MockMultipartFile("pdfFile", "test.pdf", "application/pdf", content);
        when(minioClient.putObject(any(PutObjectArgs.class))).thenReturn(null);

        String url = minioService.uploadFile(file);
        assertNotNull(url);
        assertTrue(url.contains("pdf-bucket"));
        assertTrue(url.contains("test.pdf"));
        verify(minioClient).putObject(any(PutObjectArgs.class));
    }

    @Test
    void testDownloadFileSuccess() throws Exception {
        byte[] expected = "dummy".getBytes();
        GetObjectResponse mockResponse = mock(GetObjectResponse.class);
        when(mockResponse.readAllBytes()).thenReturn(expected);
        when(minioClient.getObject(any(GetObjectArgs.class))).thenReturn(mockResponse);

        byte[] result = minioService.downloadFile("dummy.pdf");
        assertArrayEquals(expected, result);
        verify(minioClient).getObject(any(GetObjectArgs.class));
    }

    @Test
    void testDeleteFileSuccess() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String fileUrl = "http://localhost:9000/pdf-bucket/test.pdf";
        doNothing().when(minioClient).removeObject(any(RemoveObjectArgs.class));
        assertDoesNotThrow(() -> minioService.deleteFile(fileUrl));
        verify(minioClient).removeObject(any(RemoveObjectArgs.class));
    }
}
