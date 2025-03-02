package at.technikum.swkom.dms.paperlessREST.mapper;

import at.technikum.swkom.dms.paperlessREST.dto.PDFentryDto;
import at.technikum.swkom.dms.paperlessREST.entity.PDFentry;

public class PDFentryMapper {

    public static PDFentryDto mapToPDFentryDto(PDFentry pdfentry){
        return new PDFentryDto(
                pdfentry.getId(),
                pdfentry.getFileName(),
                pdfentry.getUploadDate(),
                pdfentry.getFileSize(),
                pdfentry.getFileContent(),
                pdfentry.getFileURL()
        );
    }


    public static PDFentry mapToPDFentry(PDFentryDto pdfentryDto){
        return new PDFentry(
                pdfentryDto.getId(),
                pdfentryDto.getFileName(),
                pdfentryDto.getUploadDate(),
                pdfentryDto.getFileSize(),
                pdfentryDto.getFileContent(),
                pdfentryDto.getFileURL()
        );
    }
}
