package at.technikum.swkom.dms.paperlessREST.service.impl;

import at.technikum.swkom.dms.minio.MinioService;
import at.technikum.swkom.dms.paperlessREST.dto.PDFentryDto;
import at.technikum.swkom.dms.paperlessREST.entity.PDFentry;
import at.technikum.swkom.dms.paperlessREST.exception.ResurceNotFoundException;
import at.technikum.swkom.dms.paperlessREST.mapper.PDFentryMapper;
import at.technikum.swkom.dms.paperlessREST.repository.PDFentryRepository;
import at.technikum.swkom.dms.paperlessREST.service.PDFentryService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import at.technikum.swkom.dms.RabbitMQ.messaging.RabbitMQSender;

import java.util.List;
import java.util.stream.Collectors;

import at.technikum.swkom.dms.ElasticSearch.model.PDFDocument;
import at.technikum.swkom.dms.ElasticSearch.service.PDFDocumentService;

@Service
@AllArgsConstructor
public class PDFentryServiceImpl implements PDFentryService {

    private PDFentryRepository pdfentryRepository;
    private final RabbitMQSender rabbitMQSender;
    private final MinioService minioService;
    private final PDFDocumentService pdfDocumentService;

    @Override
    public PDFentryDto createPDFentry(PDFentryDto pdFentryDto) {
        PDFentry pdFentry = PDFentryMapper.mapToPDFentry(pdFentryDto);
        PDFentry savedPDFentry = pdfentryRepository.save(pdFentry);
        //ElasticSearch update
        PDFDocument pdfDocument = new PDFDocument(
                savedPDFentry.getId().toString(),
                savedPDFentry.getFileName(),
                savedPDFentry.getUploadDate(),
                savedPDFentry.getFileSize(),
                savedPDFentry.getFileContent(),
                savedPDFentry.getFileURL()
        );
        pdfDocumentService.save(pdfDocument);
        //send the RabbitMQ Message
        rabbitMQSender.sendOCRJob(savedPDFentry.getFileURL());

        return PDFentryMapper.mapToPDFentryDto(savedPDFentry);
    }

    @Override
    public PDFentryDto getPDFentryById(Long PDFentryId) {
        PDFentry pdfentry = pdfentryRepository.findById(PDFentryId)
                .orElseThrow(() -> new ResurceNotFoundException("PDF with Id: " + PDFentryId +" does not exist"));
        return PDFentryMapper.mapToPDFentryDto(pdfentry);
    }

    @Override
    public List<PDFentryDto> getAllPDFentries() {
        List<PDFentry> PDFentries = pdfentryRepository.findAll();
        return PDFentries.stream().map((pdfentry) -> PDFentryMapper.mapToPDFentryDto(pdfentry))
                .collect(Collectors.toList());
    }

    @Override
    public PDFentryDto updatePDFentryById(Long PDFentryId, PDFentryDto updatedPDFentry) {
        PDFentry pdfentry = pdfentryRepository.findById(PDFentryId)
                .orElseThrow(() -> new ResurceNotFoundException("PDF with Id: " + PDFentryId +" does not exist"));
        pdfentry.setFileContent(updatedPDFentry.getFileContent());
        PDFentry updatedPDFentryObj = pdfentryRepository.save(pdfentry);

        //for elasticsearch
        PDFDocument pdfDocument = new PDFDocument(
                updatedPDFentryObj.getId().toString(),
                updatedPDFentryObj.getFileName(),
                updatedPDFentryObj.getUploadDate(),
                updatedPDFentryObj.getFileSize(),
                updatedPDFentryObj.getFileContent(),
                updatedPDFentryObj.getFileURL()
        );
        pdfDocumentService.save(pdfDocument);

        return PDFentryMapper.mapToPDFentryDto(updatedPDFentryObj);
    }

    @Override
    public void deletePDFentry(Long PDFentryId) {
        PDFentry pdfentry = pdfentryRepository.findById(PDFentryId)
                .orElseThrow(() -> new ResurceNotFoundException("PDF with Id: " + PDFentryId + " does not exist"));

        String fileUrl = pdfentry.getFileURL();
        if (fileUrl != null && !fileUrl.isEmpty()) {
            minioService.deleteFile(fileUrl);
        }

        pdfentryRepository.deleteById(PDFentryId);

        pdfDocumentService.deleteById(PDFentryId.toString());

        System.out.println("Deleted PDF entry and file: " + fileUrl);
    }

    @Override
    @RabbitListener(queues = "ocr_results_queue")
    public void listenToOCRResults(String message) {
        //The messages arrive in the form of http://localhost:port:URL, so we need to find the index of the 3rd colon and then trim the message 3 times
        int firstColonIndex = message.indexOf(":");

        int secondColonIndex = message.indexOf(":", firstColonIndex + 1); // Search for second colon after first one

        int thirdColonIndex = message.indexOf(":", secondColonIndex + 1);

        String fileURL = message.substring(0, thirdColonIndex).trim();

        String extractedText = message.substring(thirdColonIndex + 1).trim();

        PDFentry pdfentry = pdfentryRepository.findByFileURL(fileURL)
                .orElseThrow(() -> new ResurceNotFoundException("PDF with URL: " + fileURL + " does not exist"));

        pdfentry.setFileContent(extractedText);

        PDFentry updatedEntry = pdfentryRepository.save(pdfentry);

        PDFDocument pdfDocument = new PDFDocument(
                updatedEntry.getId().toString(),
                updatedEntry.getFileName(),
                updatedEntry.getUploadDate(),
                updatedEntry.getFileSize(),
                updatedEntry.getFileContent(),
                updatedEntry.getFileURL()
        );
        pdfDocumentService.save(pdfDocument);

        System.out.println("Updated OCR result for file URL: " + fileURL);
    }
}
