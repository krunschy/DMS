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

@Service
@AllArgsConstructor
public class PDFentryServiceImpl implements PDFentryService {

    private PDFentryRepository pdfentryRepository;
    private final RabbitMQSender rabbitMQSender;
    private final MinioService minioService;

    @Override
    public PDFentryDto createPDFentry(PDFentryDto pdFentryDto) {
        PDFentry pdFentry = PDFentryMapper.mapToPDFentry(pdFentryDto);
        PDFentry savedPDFentry = pdfentryRepository.save(pdFentry);

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
        pdfentry.setFileName(updatedPDFentry.getFileName());
        pdfentry.setUploadDate(updatedPDFentry.getUploadDate());
        pdfentry.setFileSize(updatedPDFentry.getFileSize());
        PDFentry updatedPDFentryObj = pdfentryRepository.save(pdfentry);
        return PDFentryMapper.mapToPDFentryDto(updatedPDFentryObj);
    }

    @Override
    public void deletePDFentry(Long PDFentryId) {
        PDFentry pdfentry = pdfentryRepository.findById(PDFentryId)
                .orElseThrow(() -> new ResurceNotFoundException("PDF with Id: " + PDFentryId + " does not exist"));

        // Get the file URL and delete it from MinIO
        String fileUrl = pdfentry.getFileURL();
        if (fileUrl != null && !fileUrl.isEmpty()) {
            minioService.deleteFile(fileUrl);
        }

        // Delete from database
        pdfentryRepository.deleteById(PDFentryId);

        System.out.println("Deleted PDF entry and file: " + fileUrl);
    }

    @Override
    @RabbitListener(queues = "ocr_results_queue") // Listens to the 'ocr_results_queue'
    public void listenToOCRResults(String message) {
        // Find the index of the first colon
        int firstColonIndex = message.indexOf(":");

        if (firstColonIndex == -1) {
            System.err.println("Invalid OCR result message format: no colon found.");
            return; // Invalid message format, ignore it
        }

        // Find the index of the second colon after the first one
        int secondColonIndex = message.indexOf(":", firstColonIndex + 1); // Search for second colon after first one

        if (secondColonIndex == -1) {
            System.err.println("Invalid OCR result message format: second colon not found.");
            return; // Invalid message format, ignore it
        }

        // Find the index of the third colon after the second one
        int thirdColonIndex = message.indexOf(":", secondColonIndex + 1);

        if (thirdColonIndex == -1) {
            System.err.println("Invalid OCR result message format: third colon not found.");
            return; // Invalid message format, ignore it
        }

        // Extract the fileURL (substring from start to the third colon)
        String fileURL = message.substring(0, thirdColonIndex).trim();

        // Extract the extractedText (everything after the third colon)
        String extractedText = message.substring(thirdColonIndex + 1).trim();

        // Find the entry in the database by file URL
        PDFentry pdfentry = pdfentryRepository.findByFileURL(fileURL)
                .orElseThrow(() -> new ResurceNotFoundException("PDF with URL: " + fileURL + " does not exist"));

        // Update the fileContent with the OCR result
        pdfentry.setFileContent(extractedText);

        // Save the updated entry back to the database
        pdfentryRepository.save(pdfentry);

        System.out.println("Updated OCR result for file URL: " + fileURL);
    }


}
