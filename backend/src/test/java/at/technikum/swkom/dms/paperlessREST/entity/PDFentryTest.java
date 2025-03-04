package at.technikum.swkom.dms.paperlessREST.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PDFentryTest {

    @Test
    void testGettersAndSetters() {
        PDFentry entry = new PDFentry();
        entry.setId(1L);
        entry.setFileName("file.pdf");
        entry.setUploadDate("2023-04-01");
        entry.setFileSize("1024");
        entry.setFileContent("content");
        entry.setFileURL("url");

        assertEquals(1L, entry.getId());
        assertEquals("file.pdf", entry.getFileName());
        assertEquals("2023-04-01", entry.getUploadDate());
        assertEquals("1024", entry.getFileSize());
        assertEquals("content", entry.getFileContent());
        assertEquals("url", entry.getFileURL());
    }
}
