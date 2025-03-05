package at.technikum.swkom.dms.RabbitMQ.messaging;

import at.technikum.swkom.dms.RabbitMQ.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendOCRJob(String message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "pdf.process", message);
        System.out.println("Sent message to RabbitMQ: " + message);
    }

    public void sendOCRResult(String fileName, String extractedText) {
        String resultMessage = fileName + ": " + extractedText;
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "pdf.result", resultMessage);
        System.out.println("OCR result sent to RESULT_QUEUE: " + resultMessage);
    }
}
