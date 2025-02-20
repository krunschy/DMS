package at.technikum.swkom.dms.service;

import at.technikum.swkom.dms.dto.PDFentryDto;
import at.technikum.swkom.dms.entity.PDFentry;

import java.util.List;

public interface PDFentryService {
    PDFentryDto createPDFentry(PDFentryDto pdFentryDto);

    PDFentryDto getPDFentryById(Long PDFentryId);

    List<PDFentryDto> getAllPDFentries();

    PDFentryDto updatePDFentryById(Long PDFentryId, PDFentryDto updatedPDFentry);

    void deletePDFentry(Long PDFentryId);
}
