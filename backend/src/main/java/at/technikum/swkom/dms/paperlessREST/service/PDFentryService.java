package at.technikum.swkom.dms.paperlessREST.service;

import at.technikum.swkom.dms.paperlessREST.dto.PDFentryDto;

import java.util.List;

public interface PDFentryService {
    PDFentryDto createPDFentry(PDFentryDto pdFentryDto);

    PDFentryDto getPDFentryById(Long PDFentryId);

    List<PDFentryDto> getAllPDFentries();

    PDFentryDto updatePDFentryById(Long PDFentryId, PDFentryDto updatedPDFentry);

    void deletePDFentry(Long PDFentryId);

    void listenToOCRResults(String message);
}
