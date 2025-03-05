package at.technikum.swkom.dms.ElasticSearch.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "pdf_documents")
public class PDFDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "standard")  // Use standard analyzer to lower case
    private String fileName;
    private String uploadDate;
    private String fileSize;
    private String fileContent;
    private String fileURL;

    public PDFDocument() {}

    public PDFDocument(String id, String fileName, String uploadDate, String fileSize, String fileContent, String fileURL) {
        this.id = id;
        this.fileName = fileName;
        this.uploadDate = uploadDate;
        this.fileSize = fileSize;
        this.fileContent = fileContent;
        this.fileURL = fileURL;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getUploadDate() { return uploadDate; }
    public void setUploadDate(String uploadDate) { this.uploadDate = uploadDate; }

    public String getFileSize() { return fileSize; }
    public void setFileSize(String fileSize) { this.fileSize = fileSize; }

    public String getFileContent() { return fileContent; }
    public void setFileContent(String fileContent) { this.fileContent = fileContent; }

    public String getFileURL() { return fileURL; }
    public void setFileURL(String fileURL) { this.fileURL = fileURL; }
}
