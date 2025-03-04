package at.technikum.swkom.dms.RabbitMQ.messaging;

import at.technikum.swkom.dms.RabbitMQ.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {

    @Autowired
    RabbitTemplate rabbitTemplate;

    // Send OCR job to the OCR processing queue, message is the URL
    public void sendOCRJob(String message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "pdf.process", message);
        System.out.println("Sent message to RabbitMQ: " + message);
    }

    // Send OCR result back to the result queue
    // Also remove static here
    public void sendOCRResult(String fileName, String extractedText) {
        String resultMessage = fileName + ": " + extractedText;
        // Send result to the OCR result queue
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "pdf.result", resultMessage);
        System.out.println("OCR result sent to RESULT_QUEUE: " + resultMessage);
    }
}
