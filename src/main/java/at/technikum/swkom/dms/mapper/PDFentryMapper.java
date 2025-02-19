package at.technikum.swkom.dms.mapper;

import at.technikum.swkom.dms.dto.PDFentryDto;
import at.technikum.swkom.dms.entity.PDFentry;

public class PDFentryMapper {

    public static PDFentryDto mapToPDFentryDto(PDFentry pdfentry){
        return new PDFentryDto(
                pdfentry.getId(),
                pdfentry.getFileName(),
                pdfentry.getUploadDate(),
                pdfentry.getFileSize()
        );
    }


    public static PDFentry mapToPDFentry(PDFentryDto pdfentryDto){
        return new PDFentry(
                pdfentryDto.getId(),
                pdfentryDto.getFileName(),
                pdfentryDto.getUploadDate(),
                pdfentryDto.getFileSize()
        );
    }
}
