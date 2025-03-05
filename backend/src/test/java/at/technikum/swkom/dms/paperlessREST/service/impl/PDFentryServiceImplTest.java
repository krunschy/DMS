package at.technikum.swkom.dms.paperlessREST.service.impl;

import at.technikum.swkom.dms.paperlessREST.dto.PDFentryDto;
import at.technikum.swkom.dms.paperlessREST.entity.PDFentry;
import at.technikum.swkom.dms.paperlessREST.exception.ResurceNotFoundException;
import at.technikum.swkom.dms.paperlessREST.repository.PDFentryRepository;
import at.technikum.swkom.dms.ElasticSearch.model.PDFDocument;
import at.technikum.swkom.dms.ElasticSearch.service.PDFDocumentService;
import at.technikum.swkom.dms.minio.MinioService;
import at.technikum.swkom.dms.RabbitMQ.messaging.RabbitMQSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PDFentryServiceImplTest {

    @Mock
    private PDFentryRepository pdfentryRepository;

    @Mock
    private RabbitMQSender rabbitMQSender;

    @Mock
    private MinioService minioService;

    @Mock
    private PDFDocumentService pdfDocumentService;

    @InjectMocks
    private PDFentryServiceImpl pdfentryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePDFentry() {
        PDFentryDto inputDto = new PDFentryDto(null, "file.pdf", "2023-04-01", "1024", null, "url");
        PDFentry savedEntry = new PDFentry(1L, "file.pdf", "2023-04-01", "1024", null, "url");

        when(pdfentryRepository.save(any(PDFentry.class))).thenReturn(savedEntry);
        when(pdfDocumentService.save(any(PDFDocument.class))).thenReturn(new PDFDocument());

        PDFentryDto result = pdfentryService.createPDFentry(inputDto);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(pdfentryRepository).save(any(PDFentry.class));
        verify(pdfDocumentService).save(any(PDFDocument.class));
        verify(rabbitMQSender).sendOCRJob("url");
    }

    @Test
    void testGetPDFentryByIdSuccess() {
        PDFentry entry = new PDFentry(1L, "file.pdf", "2023-04-01", "1024", "content", "url");
        when(pdfentryRepository.findById(1L)).thenReturn(Optional.of(entry));

        PDFentryDto result = pdfentryService.getPDFentryById(1L);
        assertNotNull(result);
        assertEquals("file.pdf", result.getFileName());
    }

    @Test
    void testGetPDFentryByIdNotFound() {
        when(pdfentryRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResurceNotFoundException.class, () -> pdfentryService.getPDFentryById(1L));
    }

    @Test
    void testGetAllPDFentries() {
        PDFentry entry1 = new PDFentry(1L, "file1.pdf", "2023-04-01", "1024", "content1", "url1");
        PDFentry entry2 = new PDFentry(2L, "file2.pdf", "2023-04-02", "2048", "content2", "url2");
        when(pdfentryRepository.findAll()).thenReturn(Arrays.asList(entry1, entry2));

        List<PDFentryDto> result = pdfentryService.getAllPDFentries();
        assertEquals(2, result.size());
    }

    @Test
    void testUpdatePDFentryByIdSuccess() {
        PDFentry existing = new PDFentry(1L, "file.pdf", "2023-04-01", "1024", "old content", "url");
        PDFentryDto updateDto = new PDFentryDto(null, "file.pdf", "2023-04-01", "1024", "new content", "url");
        PDFentry updated = new PDFentry(1L, "file.pdf", "2023-04-01", "1024", "new content", "url");

        when(pdfentryRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(pdfentryRepository.save(existing)).thenReturn(updated);
        when(pdfDocumentService.save(any(PDFDocument.class))).thenReturn(new PDFDocument());

        PDFentryDto result = pdfentryService.updatePDFentryById(1L, updateDto);
        assertNotNull(result);
        assertEquals("new content", result.getFileContent());
    }

    @Test
    void testUpdatePDFentryByIdNotFound() {
        when(pdfentryRepository.findById(1L)).thenReturn(Optional.empty());
        PDFentryDto updateDto = new PDFentryDto(null, "file.pdf", "2023-04-01", "1024", "new content", "url");
        assertThrows(ResurceNotFoundException.class, () -> pdfentryService.updatePDFentryById(1L, updateDto));
    }

    @Test
    void testDeletePDFentrySuccess() {
        PDFentry entry = new PDFentry(1L, "file.pdf", "2023-04-01", "1024", "content", "http://localhost:9000/pdf-bucket/test.pdf");
        when(pdfentryRepository.findById(1L)).thenReturn(Optional.of(entry));

        doNothing().when(minioService).deleteFile(anyString());
        doNothing().when(pdfentryRepository).deleteById(1L);
        doNothing().when(pdfDocumentService).deleteById("1");

        pdfentryService.deletePDFentry(1L);

        verify(minioService).deleteFile("http://localhost:9000/pdf-bucket/test.pdf");
        verify(pdfentryRepository).deleteById(1L);
        verify(pdfDocumentService).deleteById("1");
    }

    @Test
    void testDeletePDFentryNotFound() {
        when(pdfentryRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResurceNotFoundException.class, () -> pdfentryService.deletePDFentry(1L));
    }

    @Test
    void testListenToOCRResultsValidMessage() {
        // Use a test message without "http://" so the splitting works as intended.
        String validMessage = "example.com/file.pdf:ignore:ignore:Extracted OCR text";
        // The fileURL extracted will be "example.com/file.pdf:ignore:ignore"
        PDFentry pdfentry = new PDFentry(1L, "file.pdf", "2023-03-03", "1024", "old content", "example.com/file.pdf:ignore:ignore");
        when(pdfentryRepository.findByFileURL("example.com/file.pdf:ignore:ignore")).thenReturn(Optional.of(pdfentry));
        when(pdfentryRepository.save(any(PDFentry.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(pdfDocumentService.save(any(PDFDocument.class))).thenReturn(new PDFDocument());

        pdfentryService.listenToOCRResults(validMessage);

        assertEquals("Extracted OCR text", pdfentry.getFileContent());
        verify(pdfentryRepository).save(pdfentry);
        verify(pdfDocumentService).save(any(PDFDocument.class));
    }

    @Test
    void testListenToOCRResultsInvalidMessage_NoColon() {
        String invalidMessage = "invalid message";
        assertDoesNotThrow(() -> pdfentryService.listenToOCRResults(invalidMessage));
        verify(pdfentryRepository, never()).findByFileURL(anyString());
    }
}
