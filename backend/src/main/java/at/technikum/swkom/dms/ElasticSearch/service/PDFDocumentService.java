package at.technikum.swkom.dms.ElasticSearch.service;

import at.technikum.swkom.dms.ElasticSearch.model.PDFDocument;
import at.technikum.swkom.dms.ElasticSearch.repository.PDFDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PDFDocumentService {

    private final PDFDocumentRepository pdfDocumentRepository;

    @Autowired
    public PDFDocumentService(PDFDocumentRepository pdfDocumentRepository) {
        this.pdfDocumentRepository = pdfDocumentRepository;
    }

    public PDFDocument save(PDFDocument document) {
        return pdfDocumentRepository.save(document);
    }

    public void deleteById(String id) {
        pdfDocumentRepository.deleteById(id);
    }

    public Iterable<PDFDocument> findAll() {
        return pdfDocumentRepository.findAll();
    }
}
