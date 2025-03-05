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
    private RabbitMQSender rabbitMQSender;

    @Autowired
    private MinioService minioService;

    public static void main(String[] args) {
        SpringApplication.run(OCRWorkerApplication.class, args);
    }

    @RabbitListener(queues = "pdf-processing-queue")
    public void processPDF(String pdfFileUrl) {
        System.out.println("Received PDF URL for OCR: " + pdfFileUrl);

        String fileName = extractFileNameFromURL(pdfFileUrl);

        byte[] pdfData = minioService.downloadFile(fileName);

        String extractedText = OCRService.performOCR(pdfData);

        rabbitMQSender.sendOCRResult(pdfFileUrl, extractedText);
    }

    private String extractFileNameFromURL(String pdfFileUrl) {
        try {
            String[] urlParts = pdfFileUrl.split("/");
            return urlParts[urlParts.length - 1];
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract file name from URL: " + pdfFileUrl, e);
        }
    }
}
