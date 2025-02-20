package at.technikum.swkom.dms.controller;

import at.technikum.swkom.dms.dto.PDFentryDto;
import at.technikum.swkom.dms.entity.PDFentry;
import at.technikum.swkom.dms.service.PDFentryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/PDFentries") //no idea if this is the correct link, just from the tutorial
public class PDFentryController {

    private PDFentryService pdFentryService;

    //Build add PDF REST API
    @PostMapping
    public ResponseEntity<PDFentryDto> createPDFentry(@RequestBody PDFentryDto pdFentryDto){
        PDFentryDto savedPDFentry = pdFentryService.createPDFentry(pdFentryDto);
        return new ResponseEntity<>(savedPDFentry, HttpStatus.CREATED);
    }

    //Build get PDF Rest API
    @GetMapping("{id}")
    public ResponseEntity<PDFentryDto> getPDFentryById(@PathVariable("id") Long Id){
        PDFentryDto pdFentryDto = pdFentryService.getPDFentryById(Id);
        return ResponseEntity.ok(pdFentryDto);
    }

    //Build getAll PDF
    @GetMapping
    public ResponseEntity<List<PDFentryDto>> getAllPDFentries(){
        List<PDFentryDto> pdfentries = pdFentryService.getAllPDFentries();
        return ResponseEntity.ok(pdfentries);
    }

    //Build update PDFentry
    @PutMapping("{id}")
    public ResponseEntity<PDFentryDto> updatePDFentry(@PathVariable("id") Long Id, @RequestBody PDFentryDto updatedPdfentry) {
        PDFentryDto pdfentryDto = pdFentryService.updatePDFentryById(Id, updatedPdfentry);
        return ResponseEntity.ok(pdfentryDto);
    }

    //Build delete PDFentry
    @DeleteMapping("{id}")
    public ResponseEntity<String> deletePDFentry(@PathVariable("id") Long Id){
    pdFentryService.deletePDFentry(Id);
    return ResponseEntity.ok("PDF deleted successfully");
    }
}
