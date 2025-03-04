package at.technikum.swkom.dms.paperlessREST.mapper;

import at.technikum.swkom.dms.paperlessREST.dto.PDFentryDto;
import at.technikum.swkom.dms.paperlessREST.entity.PDFentry;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PDFentryMapperTest {

    @Test
    void testMapToPDFentryDto() {
        PDFentry entry = new PDFentry(1L, "file.pdf", "2023-04-01", "1024", "content", "url");
        PDFentryDto dto = PDFentryMapper.mapToPDFentryDto(entry);

        assertEquals(entry.getId(), dto.getId());
        assertEquals(entry.getFileName(), dto.getFileName());
        assertEquals(entry.getUploadDate(), dto.getUploadDate());
        assertEquals(entry.getFileSize(), dto.getFileSize());
        assertEquals(entry.getFileContent(), dto.getFileContent());
        assertEquals(entry.getFileURL(), dto.getFileURL());
    }

    @Test
    void testMapToPDFentry() {
        PDFentryDto dto = new PDFentryDto(1L, "file.pdf", "2023-04-01", "1024", "content", "url");
        PDFentry entry = PDFentryMapper.mapToPDFentry(dto);

        assertEquals(dto.getId(), entry.getId());
        assertEquals(dto.getFileName(), entry.getFileName());
        assertEquals(dto.getUploadDate(), entry.getUploadDate());
        assertEquals(dto.getFileSize(), entry.getFileSize());
        assertEquals(dto.getFileContent(), entry.getFileContent());
        assertEquals(dto.getFileURL(), entry.getFileURL());
    }
}
