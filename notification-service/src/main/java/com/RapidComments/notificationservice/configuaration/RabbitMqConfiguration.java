package com.RapidComments.notificationservice.configuaration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class RabbitMqConfiguration {


    public static final String QUEUE = "notification_queue";
    public static final String EMAIL_QUEUE = "email_queue";
    public static final String EXCHANGE = "message_exchange";
    public static final String ROUTING_KEY = "notification_routingKey";
    public static final String EMAIL_ROUTING_KEY = "email_queue_routingKey";

    @Bean
    public Queue queue() {
        return  new Queue(QUEUE);
    }

    @Bean
    public Queue queueEmail() {
        return  new Queue(EMAIL_QUEUE);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding notifyBinding(Queue queue, DirectExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public Binding emailBinding(){
        return BindingBuilder
                .bind(queueEmail())
                .to(exchange())
                .with(EMAIL_ROUTING_KEY);
    }


    @Bean
    public MessageConverter messageConverter() {
        return  new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return  template;
    }

}