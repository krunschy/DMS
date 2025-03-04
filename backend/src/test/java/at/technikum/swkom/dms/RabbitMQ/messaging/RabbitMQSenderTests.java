package at.technikum.swkom.dms.RabbitMQ.messaging;

import at.technikum.swkom.dms.RabbitMQ.config.RabbitMQConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

class RabbitMQSenderTests {

    @Mock
    private RabbitTemplate rabbitTemplate;

    private RabbitMQSender rabbitMQSender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rabbitMQSender = new RabbitMQSender();
        rabbitMQSender.rabbitTemplate = rabbitTemplate;
    }

    @Test
    void testSendOCRJob() {
        String message = "test-file.pdf";
        rabbitMQSender.sendOCRJob(message);
        verify(rabbitTemplate).convertAndSend(eq(RabbitMQConfig.EXCHANGE_NAME), eq("pdf.process"), eq(message));
    }

    @Test
    void testSendOCRResult() {
        String fileName = "test.pdf";
        String extractedText = "Sample text";
        rabbitMQSender.sendOCRResult(fileName, extractedText);
        verify(rabbitTemplate).convertAndSend(eq(RabbitMQConfig.EXCHANGE_NAME), eq("pdf.result"), eq(fileName + ": " + extractedText));
    }
}
