package at.technikum.swkom.dms.paperlessREST.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PDFentryDtoTest {

    @Test
    void testGettersAndSetters() {
        PDFentryDto dto = new PDFentryDto();
        dto.setId(1L);
        dto.setFileName("test.pdf");
        dto.setUploadDate("2023-04-01");
        dto.setFileSize("1024");
        dto.setFileContent("content");
        dto.setFileURL("url");

        assertEquals(1L, dto.getId());
        assertEquals("test.pdf", dto.getFileName());
        assertEquals("2023-04-01", dto.getUploadDate());
        assertEquals("1024", dto.getFileSize());
        assertEquals("content", dto.getFileContent());
        assertEquals("url", dto.getFileURL());
    }
}
