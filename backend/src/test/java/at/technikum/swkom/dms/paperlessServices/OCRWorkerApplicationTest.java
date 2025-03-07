package at.technikum.swkom.dms.paperlessServices;

import at.technikum.swkom.dms.RabbitMQ.messaging.RabbitMQSender;
import at.technikum.swkom.dms.minio.MinioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OCRWorkerApplicationTest {

    @Mock
    private MinioService minioService;

    @Mock
    private RabbitMQSender rabbitMQSender;

    @InjectMocks
    private OCRWorkerApplication ocrWorkerApplication;

    @Test
    void testProcessPDF() {
        String pdfUrl = "http://localhost:9000/pdf-bucket/test.pdf";
        String expectedFileName = "test.pdf";
        byte[] fakePdfData = "fake pdf data".getBytes();

        when(minioService.downloadFile(expectedFileName)).thenReturn(fakePdfData);

        try (MockedStatic<OCRService> ocrServiceMock = mockStatic(OCRService.class)) {
            ocrServiceMock.when(() -> OCRService.performOCR(fakePdfData))
                    .thenReturn("Dummy OCR Text");

            ocrWorkerApplication.processPDF(pdfUrl);

            verify(minioService).downloadFile(expectedFileName);
            verify(rabbitMQSender).sendOCRResult(pdfUrl, "Dummy OCR Text");
        }
    }

    @Test
    void testExtractFileNameFromURL_WithValidUrl() {
        String pdfUrl = "http://localhost:9000/pdf-bucket/sample.pdf";
        String expectedFileName = "sample.pdf";
        try {
            java.lang.reflect.Method method = OCRWorkerApplication.class.getDeclaredMethod("extractFileNameFromURL", String.class);
            method.setAccessible(true);
            String result = (String) method.invoke(ocrWorkerApplication, pdfUrl);
            assertEquals(expectedFileName, result);
        } catch (Exception e) {
            fail("Reflection failed: " + e.getMessage());
        }
    }

    @Test
    void testExtractFileNameFromURL_WithInvalidUrl() {
        String invalidUrl = "invalidUrlWithoutSlash";
        try {
            java.lang.reflect.Method method = OCRWorkerApplication.class.getDeclaredMethod("extractFileNameFromURL", String.class);
            method.setAccessible(true);
            String result = (String) method.invoke(ocrWorkerApplication, invalidUrl);
            assertEquals(invalidUrl, result, "The method should return the input string if no slash is found");
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

}
