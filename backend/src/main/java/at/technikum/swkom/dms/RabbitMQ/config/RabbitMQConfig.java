package at.technikum.swkom.dms.RabbitMQ.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "pdf-processing-queue";
    public static final String EXCHANGE_NAME = "pdf-exchange";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true);  // Durable queue
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("pdf.process");
    }
}
