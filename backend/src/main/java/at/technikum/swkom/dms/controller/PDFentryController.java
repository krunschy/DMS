package at.technikum.swkom.dms.controller;

import at.technikum.swkom.dms.dto.PDFentryDto;
import at.technikum.swkom.dms.entity.PDFentry;
import at.technikum.swkom.dms.minio.MinioService;
import at.technikum.swkom.dms.service.PDFentryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/PDFentries")
public class PDFentryController {

    private PDFentryService pdfEntryService;
    private MinioService minioService;

    //Build add PDF REST API
    /*@PostMapping
    public ResponseEntity<PDFentryDto> createPDFentry(@RequestBody PDFentryDto pdFentryDto){
        PDFentryDto savedPDFentry = pdfEntryService.createPDFentry(pdFentryDto);
        return new ResponseEntity<>(savedPDFentry, HttpStatus.CREATED);
    }*/
    @PostMapping
    public ResponseEntity<String> uploadPDF(
            @RequestParam("pdfFile") MultipartFile pdfFile,
            @RequestParam("fileName") String fileName,
            @RequestParam("fileSize") String fileSize,
            @RequestParam("uploadDate") String uploadDate) {

        try {
            // Upload the PDF file to MinIO
            String fileUrl = minioService.uploadFile(pdfFile);

            PDFentryDto savedPDFentry = pdfEntryService.createPDFentry(new PDFentryDto(null, fileName, uploadDate, fileSize, null, fileUrl));

            pdfEntryService.createPDFentry(savedPDFentry);

            return new ResponseEntity<>("File uploaded successfully: " + fileUrl, HttpStatus.CREATED);

        } catch (IOException e) {
            return new ResponseEntity<>("File upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //Build get PDF Rest API
    @GetMapping("{id}")
    public ResponseEntity<PDFentryDto> getPDFentryById(@PathVariable("id") Long Id){
        PDFentryDto pdFentryDto = pdfEntryService.getPDFentryById(Id);
        return ResponseEntity.ok(pdFentryDto);
    }

    //Build getAll PDF
    @GetMapping
    public ResponseEntity<List<PDFentryDto>> getAllPDFentries(){
        List<PDFentryDto> pdfentries = pdfEntryService.getAllPDFentries();
        return ResponseEntity.ok(pdfentries);
    }

    //Build update PDFentry, Das muss noch für content geupped werden, glaub ich
    @PutMapping("{id}")
    public ResponseEntity<PDFentryDto> updatePDFentry(@PathVariable("id") Long Id, @RequestBody PDFentryDto updatedPdfentry) {
        PDFentryDto pdfentryDto = pdfEntryService.updatePDFentryById(Id, updatedPdfentry);
        return ResponseEntity.ok(pdfentryDto);
    }

    //Build delete PDFentry
    @DeleteMapping("{id}")
    public ResponseEntity<String> deletePDFentry(@PathVariable("id") Long Id){
    pdfEntryService.deletePDFentry(Id);
    return ResponseEntity.ok("PDF deleted successfully");
    }
}
