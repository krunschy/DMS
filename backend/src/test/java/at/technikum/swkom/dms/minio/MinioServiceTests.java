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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
        // Construct the service (its constructor may create a new client internally)
        minioService = new MinioService(minioClient);
        // Override the internal client with our mock
        ReflectionTestUtils.setField(minioService, "minioClient", minioClient);
    }

    @Test
    void testUploadFileSuccess() throws Exception {
        byte[] content = "file content".getBytes();
        MockMultipartFile file = new MockMultipartFile("pdfFile", "test.pdf", "application/pdf", content);
        // Since putObject() returns a value, stub it using when().thenReturn(null)
        when(minioClient.putObject(any(PutObjectArgs.class))).thenReturn(null);

        String url = minioService.uploadFile(file);
        assertNotNull(url);
        // The returned URL should include the file name and bucket name
        assertTrue(url.contains("pdf-bucket"));
        assertTrue(url.contains("test.pdf"));
        verify(minioClient).putObject(any(PutObjectArgs.class));
    }

    @Test
    void testDownloadFileSuccess() throws Exception {
        byte[] expected = "dummy".getBytes();
        // Create a mock GetObjectResponse that returns our expected bytes
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
        // Stub removeObject() to do nothing (it’s a void method)
        doNothing().when(minioClient).removeObject(any(RemoveObjectArgs.class));
        assertDoesNotThrow(() -> minioService.deleteFile(fileUrl));
        verify(minioClient).removeObject(any(RemoveObjectArgs.class));
    }
}
