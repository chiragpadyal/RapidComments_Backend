package com.RapidComments.notificationservice.scheduler;

import com.RapidComments.notificationservice.model.EmailAggregator;
import com.RapidComments.notificationservice.model.EmailModel;
import com.RapidComments.notificationservice.repository.EmailRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class EmailScheduler {
    private final Map<String, EmailAggregator> emailAggregators = new HashMap<>();
    @Autowired
    private RabbitTemplate template;

    @Autowired
    private EmailRepo emailRepo;
    @Scheduled(cron = "@daily")
    public void scheduledMethod() {
        //aggregate notification from db
        List<EmailModel> unReadEmails = emailRepo.findByIsRead(false);

        for(EmailModel emailModel: unReadEmails){
            String emailId = emailModel.getEmailId();
            EmailAggregator aggregator = emailAggregators.getOrDefault(emailId, new EmailAggregator(emailId));
            aggregator.addEmail(emailModel);
            log.info("Aggregated content for email ID {}: {}", emailId, aggregator.getAggregatedContent());
            emailAggregators.put(emailId, aggregator);
        }

        // send request to put in queue
/*
        template.convertAndSend(RabbitMqConfiguration.EXCHANGE,
                RabbitMqConfiguration.EMAIL_ROUTING_KEY, emailModel);
*/

    }
}
