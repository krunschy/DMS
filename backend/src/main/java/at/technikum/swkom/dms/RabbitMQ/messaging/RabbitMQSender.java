package at.technikum.swkom.dms.RabbitMQ.messaging;
import at.technikum.swkom.dms.RabbitMQ.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "pdf.process", message);
        System.out.println("Sent message to RabbitMQ: " + message);
    }
}
