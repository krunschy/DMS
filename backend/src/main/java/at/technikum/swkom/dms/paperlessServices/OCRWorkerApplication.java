package at.technikum.swkom.dms.paperlessServices;

import at.technikum.swkom.dms.RabbitMQ.messaging.RabbitMQSender;
import at.technikum.swkom.dms.minio.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "at.technikum.swkom.dms.RabbitMQ.messaging")
@ComponentScan(basePackages = "at.technikum.swkom.dms.minio")
public class OCRWorkerApplication {

    @Autowired
    private RabbitMQSender rabbitMQSender;  // Autowire RabbitMQSender

    @Autowired
    private MinioService minioService;  // Autowire MinIOClientService

    public static void main(String[] args) {
        SpringApplication.run(OCRWorkerApplication.class, args);
    }

    @RabbitListener(queues = "pdf-processing-queue")
    public void processPDF(String pdfFileUrl) {
        System.out.println("Received PDF URL for OCR: " + pdfFileUrl);

        // Extract the file name from the URL (e.g., 9f5e8a91-6c9f-45de-abbe-b000c31196ec-RubiconMotivationsschreiben.pdf)
        String fileName = extractFileNameFromURL(pdfFileUrl);

        // Download from MinIO using the extracted file name
        byte[] pdfData = minioService.downloadFile(fileName);  // Use instance method

        // Perform OCR
        String extractedText = OCRService.performOCR(pdfData);

        // Send back OCR result using autowired RabbitMQSender instance
        rabbitMQSender.sendOCRResult(pdfFileUrl, extractedText);
    }

    // Helper method to extract the file name from the URL
    private String extractFileNameFromURL(String pdfFileUrl) {
        try {
            // Get the file name from the URL by extracting everything after the last "/"
            String[] urlParts = pdfFileUrl.split("/");
            return urlParts[urlParts.length - 1];
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract file name from URL: " + pdfFileUrl, e);
        }
    }
}
