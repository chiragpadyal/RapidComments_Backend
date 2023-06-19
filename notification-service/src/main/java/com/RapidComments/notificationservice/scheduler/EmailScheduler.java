package com.RapidComments.notificationservice.scheduler;

import com.RapidComments.notificationservice.configuaration.RabbitMqConfiguration;
import com.RapidComments.notificationservice.model.EmailAggregator;
import com.RapidComments.notificationservice.model.EmailModel;
import com.RapidComments.notificationservice.repository.EmailRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Component
public class EmailScheduler {
    private final Map<String, EmailAggregator> emailAggregators = new HashMap<>();
    @Value("${email-server-url}")
    private String emailServerUrl = "";

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private EmailRepo emailRepo;

    @Scheduled(cron = "${mail-trap.cron}")
    public void aggregateEmails() {
//        RestTemplate restTemplate = new RestTemplate();

        log.info("Current time is :: " + LocalDateTime.now());
        List<EmailModel> unReadEmails = emailRepo.findByIsRead(false);

        //aggregate notification from db
        for(EmailModel emailModel: unReadEmails){
            String emailId = emailModel.getEmailId();
            EmailAggregator aggregator = emailAggregators.getOrDefault(emailId, new EmailAggregator(emailId));
            aggregator.addEmail(emailModel);
            emailAggregators.put(emailId, aggregator);
        }

        for ( Map.Entry<String, EmailAggregator> mapElement : emailAggregators.entrySet()) {
            // of same email ID
            Iterator<Collection<List<EmailModel>>> groupByThreadName =  mapElement.getValue().fetchSameThreadEmail();
            for (Iterator<Collection<List<EmailModel>>> it = groupByThreadName; it.hasNext(); ) {
                // of same thread
                List<List<EmailModel>> groupOfSameThreadName = new ArrayList(it.next());

                template.convertAndSend(RabbitMqConfiguration.EXCHANGE,
                        RabbitMqConfiguration.EMAIL_ROUTING_KEY, groupOfSameThreadName);

//                restTemplate.postForEntity(emailServerUrl, groupOfSameThreadName, String.class);
            }
        }
    }
}
