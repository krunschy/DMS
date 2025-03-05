package at.technikum.swkom.dms.ElasticSearch.service;

import at.technikum.swkom.dms.ElasticSearch.model.PDFDocument;
import at.technikum.swkom.dms.ElasticSearch.repository.PDFDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class PDFDocumentService {

    private static PDFDocumentRepository pdfDocumentRepository = null;

    @Autowired
    public PDFDocumentService(PDFDocumentRepository pdfDocumentRepository) {
        this.pdfDocumentRepository = pdfDocumentRepository;
    }

    public static List<PDFDocument> searchDocumentsByNameAndContent(String searchText) {
        String lowercaseSearchText = searchText.toLowerCase();
        return pdfDocumentRepository.searchByNameAndContent(lowercaseSearchText);
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
