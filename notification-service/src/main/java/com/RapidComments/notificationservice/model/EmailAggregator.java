package com.RapidComments.notificationservice.model;

import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EmailAggregator {
    private String emailId;
    private List<EmailModel> emails;

    public EmailAggregator(String emailId) {
        this.emailId = emailId;
        this.emails = new ArrayList<>();
    }

    public void addEmail(EmailModel email) {
        emails.add(email);
    }

    public String getAggregatedContent() {
        StringBuilder aggregatedContent = new StringBuilder();
        for (EmailModel email : emails) {
            aggregatedContent.append(email.getMessageBody()).append("\n");
        }
        return aggregatedContent.toString();
    }

/*    @Scheduled(fixedRate = 24 * 60 * 60 * 1000) // Execute every 24 hours
    public void processAggregatedEmail() {
        if (!emails.isEmpty() && isReadyForProcessing()) {
            // final run
            emails.clear();
        }
    }*/
}
