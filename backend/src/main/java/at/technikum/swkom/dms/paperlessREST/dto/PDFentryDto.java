package at.technikum.swkom.dms.paperlessREST.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PDFentryDto {
    private Long id;
    private String fileName;
    private String uploadDate;
    private String fileSize;
    private String fileContent;
    private String fileURL;
}
