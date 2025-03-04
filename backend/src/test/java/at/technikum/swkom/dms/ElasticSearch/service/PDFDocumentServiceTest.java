package at.technikum.swkom.dms.ElasticSearch.service;

import at.technikum.swkom.dms.ElasticSearch.model.PDFDocument;
import at.technikum.swkom.dms.ElasticSearch.repository.PDFDocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PDFDocumentServiceTest {

    @Mock
    private PDFDocumentRepository repository;

    @InjectMocks
    private PDFDocumentService pdfDocumentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        PDFDocument document = new PDFDocument("1", "file.pdf", "2023-04-01", "1024", "Content", "http://example.com/file.pdf");
        when(repository.save(document)).thenReturn(document);
        PDFDocument saved = pdfDocumentService.save(document);
        assertNotNull(saved);
        assertEquals("1", saved.getId());
        verify(repository).save(document);
    }

    @Test
    void testDeleteById() {
        String id = "1";
        pdfDocumentService.deleteById(id);
        verify(repository).deleteById(id);
    }

    @Test
    void testFindAll() {
        when(repository.findAll()).thenReturn(java.util.List.of(
                new PDFDocument("1", "a.pdf", "2023-04-01", "1024", "Content A", "urlA"),
                new PDFDocument("2", "b.pdf", "2023-04-02", "2048", "Content B", "urlB")
        ));
        Iterable<PDFDocument> docs = pdfDocumentService.findAll();
        int count = 0;
        for(PDFDocument doc : docs) {
            count++;
        }
        assertEquals(2, count);
        verify(repository).findAll();
    }
}
