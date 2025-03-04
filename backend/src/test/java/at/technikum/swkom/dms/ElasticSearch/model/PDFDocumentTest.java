package at.technikum.swkom.dms.ElasticSearch.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PDFDocumentTest {

    @Test
    void testDefaultConstructorAndSetters() {
        PDFDocument document = new PDFDocument();
        document.setId("1");
        document.setFileName("example.pdf");
        document.setUploadDate("2023-04-01");
        document.setFileSize("1024");
        document.setFileContent("Sample Content");
        document.setFileURL("http://example.com/example.pdf");

        assertEquals("1", document.getId());
        assertEquals("example.pdf", document.getFileName());
        assertEquals("2023-04-01", document.getUploadDate());
        assertEquals("1024", document.getFileSize());
        assertEquals("Sample Content", document.getFileContent());
        assertEquals("http://example.com/example.pdf", document.getFileURL());
    }

    @Test
    void testAllArgsConstructor() {
        PDFDocument document = new PDFDocument("2", "file.pdf", "2023-04-02", "2048", "Content", "http://example.com/file.pdf");
        assertEquals("2", document.getId());
        assertEquals("file.pdf", document.getFileName());
        assertEquals("2023-04-02", document.getUploadDate());
        assertEquals("2048", document.getFileSize());
        assertEquals("Content", document.getFileContent());
        assertEquals("http://example.com/file.pdf", document.getFileURL());
    }
}
