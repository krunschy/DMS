package at.technikum.swkom.dms.service.impl;

import at.technikum.swkom.dms.dto.PDFentryDto;
import at.technikum.swkom.dms.entity.PDFentry;
import at.technikum.swkom.dms.exception.ResurceNotFoundException;
import at.technikum.swkom.dms.mapper.PDFentryMapper;
import at.technikum.swkom.dms.repository.PDFentryRepository;
import at.technikum.swkom.dms.service.PDFentryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import at.technikum.swkom.dms.RabbitMQ.messaging.RabbitMQSender;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PDFentryServiceImpl implements PDFentryService {

    private PDFentryRepository pdfentryRepository;
    private final RabbitMQSender rabbitMQSender;

    @Override
    public PDFentryDto createPDFentry(PDFentryDto pdFentryDto) {
        PDFentry pdFentry = PDFentryMapper.mapToPDFentry(pdFentryDto);
        PDFentry savedPDFentry = pdfentryRepository.save(pdFentry);

        String message = "New PDF uploaded: " + savedPDFentry.getFileName();
        rabbitMQSender.sendMessage(message);

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
                .orElseThrow(() -> new ResurceNotFoundException("PDF with Id: " + PDFentryId +" does not exist"));
        pdfentryRepository.deleteById(PDFentryId);
    }

}
