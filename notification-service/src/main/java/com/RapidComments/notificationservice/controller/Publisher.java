package com.RapidComments.notificationservice.controller;

import com.RapidComments.notificationservice.configuaration.RabbitMqConfiguration;
import com.RapidComments.notificationservice.model.EmailModel;
import com.RapidComments.notificationservice.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/notify")
public class Publisher {
    private RabbitTemplate template;
    private final EmailService mailtrapClient;

    public Publisher(EmailService mailtrapClient, RabbitTemplate template) {
        this.mailtrapClient = mailtrapClient;
        this.template = template;
    }

    @PostMapping("/push-notify")
    public String publishNotification(@RequestBody EmailModel emailModel) {
        template.convertAndSend(RabbitMqConfiguration.EXCHANGE,
                RabbitMqConfiguration.ROUTING_KEY, emailModel);
        return "Notification Published";
    }


    @PostMapping("/push-mail")
    public String publishEmail(@RequestBody EmailModel[][] emailModels) {
        template.convertAndSend(RabbitMqConfiguration.EXCHANGE,
                RabbitMqConfiguration.EMAIL_ROUTING_KEY, emailModels);
        return "Email Published";
    }

}