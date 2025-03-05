package at.technikum.swkom.dms.paperlessREST.controller;

import at.technikum.swkom.dms.ElasticSearch.model.PDFDocument;
import at.technikum.swkom.dms.ElasticSearch.service.PDFDocumentService;
import at.technikum.swkom.dms.paperlessREST.dto.PDFentryDto;
import at.technikum.swkom.dms.minio.MinioService;
import at.technikum.swkom.dms.paperlessREST.service.PDFentryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/PDFentries")
public class PDFentryController {

    private PDFentryService pdfEntryService;
    private MinioService minioService;

    @PostMapping
    public ResponseEntity<String> uploadPDF(
            @RequestParam("pdfFile") MultipartFile pdfFile,
            @RequestParam("fileName") String fileName,
            @RequestParam("fileSize") String fileSize,
            @RequestParam("uploadDate") String uploadDate) {
            String fileUrl = minioService.uploadFile(pdfFile);

            PDFentryDto savedPDFentry = pdfEntryService.createPDFentry(new PDFentryDto(null, fileName, uploadDate, fileSize, null, fileUrl));

            pdfEntryService.createPDFentry(savedPDFentry);

            return new ResponseEntity<>("File uploaded successfully: " + fileUrl, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<PDFentryDto> getPDFentryById(@PathVariable("id") Long Id){
        PDFentryDto pdFentryDto = pdfEntryService.getPDFentryById(Id);
        return ResponseEntity.ok(pdFentryDto);
    }

    @GetMapping
    public ResponseEntity<List<PDFentryDto>> getAllPDFentries(){
        List<PDFentryDto> pdfentries = pdfEntryService.getAllPDFentries();
        return ResponseEntity.ok(pdfentries);
    }

    @PutMapping("{id}")
    public ResponseEntity<PDFentryDto> updatePDFentry(@PathVariable("id") Long Id, @RequestBody PDFentryDto updatedPdfentry) {
        PDFentryDto pdfentryDto = pdfEntryService.updatePDFentryById(Id, updatedPdfentry);
        return ResponseEntity.ok(pdfentryDto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deletePDFentry(@PathVariable("id") Long Id){
    pdfEntryService.deletePDFentry(Id);
    return ResponseEntity.ok("PDF deleted successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<List<PDFDocument>> searchPDFs(@RequestParam String query) {
        List<PDFDocument> results = PDFDocumentService.searchDocumentsByNameAndContent(query);
        return ResponseEntity.ok(results);
    }
}
