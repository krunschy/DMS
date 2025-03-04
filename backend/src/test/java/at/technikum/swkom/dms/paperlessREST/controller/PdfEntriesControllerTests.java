package at.technikum.swkom.dms.paperlessREST.controller;

import at.technikum.swkom.dms.paperlessREST.dto.PDFentryDto;
import at.technikum.swkom.dms.paperlessREST.service.PDFentryService;
import at.technikum.swkom.dms.minio.MinioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PDFentryControllerTest {

    @Mock
    private PDFentryService pdfEntryService;

    @Mock
    private MinioService minioService;

    @InjectMocks
    private PDFentryController pdfentryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUploadPDF() {
        MockMultipartFile file = new MockMultipartFile("pdfFile", "test.pdf", "application/pdf", "content".getBytes());
        String fileName = "test.pdf";
        String fileSize = "1024";
        String uploadDate = "2023-04-01";
        String fileUrl = "http://localhost:9000/pdf-bucket/test.pdf";

        when(minioService.uploadFile(file)).thenReturn(fileUrl);
        PDFentryDto dto = new PDFentryDto(1L, fileName, uploadDate, fileSize, null, fileUrl);
        when(pdfEntryService.createPDFentry(any(PDFentryDto.class))).thenReturn(dto);

        ResponseEntity<String> response = pdfentryController.uploadPDF(file, fileName, fileSize, uploadDate);
        assertEquals(201, response.getStatusCodeValue());
        assertTrue(response.getBody().contains(fileUrl));
        verify(pdfEntryService, times(2)).createPDFentry(any(PDFentryDto.class));
    }

    @Test
    void testGetPDFentryById() {
        PDFentryDto dto = new PDFentryDto(1L, "test.pdf", "2023-04-01", "1024", "content", "url");
        when(pdfEntryService.getPDFentryById(1L)).thenReturn(dto);

        ResponseEntity<PDFentryDto> response = pdfentryController.getPDFentryById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dto, response.getBody());
    }

    @Test
    void testGetAllPDFentries() {
        PDFentryDto dto1 = new PDFentryDto(1L, "a.pdf", "2023-04-01", "1024", "contentA", "urlA");
        PDFentryDto dto2 = new PDFentryDto(2L, "b.pdf", "2023-04-02", "2048", "contentB", "urlB");
        when(pdfEntryService.getAllPDFentries()).thenReturn(List.of(dto1, dto2));

        ResponseEntity<List<PDFentryDto>> response = pdfentryController.getAllPDFentries();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testUpdatePDFentry() {
        PDFentryDto updateDto = new PDFentryDto(1L, "file.pdf", "2023-04-01", "1024", "new content", "url");
        when(pdfEntryService.updatePDFentryById(1L, updateDto)).thenReturn(updateDto);

        ResponseEntity<PDFentryDto> response = pdfentryController.updatePDFentry(1L, updateDto);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("new content", response.getBody().getFileContent());
    }

    @Test
    void testDeletePDFentry() {
        doNothing().when(pdfEntryService).deletePDFentry(1L);
        ResponseEntity<String> response = pdfentryController.deletePDFentry(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("PDF deleted successfully"));
    }
}
