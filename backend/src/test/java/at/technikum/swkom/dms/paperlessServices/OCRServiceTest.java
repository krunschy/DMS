package at.technikum.swkom.dms.paperlessServices;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OCRServiceTest {

    @Test
    void testPerformOCRWithInvalidData() {
        byte[] invalidData = "not a valid pdf".getBytes();
        Exception exception = assertThrows(RuntimeException.class, () -> {
            OCRService.performOCR(invalidData);
        });
        assertTrue(exception.getMessage().contains("OCR failed"));
    }
}
