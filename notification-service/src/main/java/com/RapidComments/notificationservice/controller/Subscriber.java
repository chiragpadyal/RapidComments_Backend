package com.RapidComments.notificationservice.controller;

import com.RapidComments.notificationservice.configuaration.RabbitMqConfiguration;
import com.RapidComments.notificationservice.model.EmailModel;
import com.RapidComments.notificationservice.repository.EmailRepo;
import com.RapidComments.notificationservice.services.EmailService;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@Slf4j
public class Subscriber {
    private final EmailService mailtrapClient;
    public Subscriber(EmailService mailtrapClient) {
        this.mailtrapClient = mailtrapClient;
    }


    @Autowired
    private EmailRepo emailRepo;
    @RabbitListener(queues = RabbitMqConfiguration.QUEUE)
    public void receiveNotification(@Payload EmailModel emailModel) {
        //save notification to db
        emailRepo.save(emailModel);

    }

    @RabbitListener(queues = RabbitMqConfiguration.EMAIL_QUEUE)
    public void receiveMail(@Payload EmailModel[][] emailModels) {
        try {
            mailtrapClient.sendEmail(emailModels);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
