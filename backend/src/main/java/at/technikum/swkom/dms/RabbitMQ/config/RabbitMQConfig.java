package at.technikum.swkom.dms.RabbitMQ.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

@Configuration
public class RabbitMQConfig {

    public static final String PROCESSING_QUEUE_NAME = "pdf-processing-queue";
    public static final String RESULT_QUEUE_NAME = "ocr_results_queue";
    public static final String EXCHANGE_NAME = "pdf-exchange";

    @Bean
    public Queue processingQueue() {
        return new Queue(PROCESSING_QUEUE_NAME, true);
    }

    @Bean
    public Queue resultQueue() {
        return new Queue(RESULT_QUEUE_NAME, true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding processingBinding(Queue processingQueue, DirectExchange exchange) {
        return BindingBuilder.bind(processingQueue).to(exchange).with("pdf.process");
    }

    @Bean
    public Binding resultBinding(Queue resultQueue, DirectExchange exchange) {
        return BindingBuilder.bind(resultQueue).to(exchange).with("pdf.result");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }
}
