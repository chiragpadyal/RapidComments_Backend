package com.RapidComments.notificationservice.model;

import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.*;

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

    public Iterator<Collection<List<EmailModel>>> fetchSameThreadEmail() {
        Map<String, Map<String, List<EmailModel>>> threadSubThreadEmailMap = new HashMap<>();

        for (EmailModel email : emails) {
            String threadName = email.getThreadName();
            String subThreadName = email.getSubThreadName();

            Map<String, List<EmailModel>> subThreadEmailMap = threadSubThreadEmailMap.getOrDefault(threadName, new HashMap<>());
            List<EmailModel> subThreadEmails = subThreadEmailMap.getOrDefault(subThreadName, new ArrayList<>());
            subThreadEmails.add(email);

            subThreadEmailMap.put(subThreadName, subThreadEmails);
            threadSubThreadEmailMap.put(threadName, subThreadEmailMap);
        }

        return threadSubThreadEmailMap.values().stream().map(Map::values).iterator();
    }



/*    @Scheduled(fixedRate = 24 * 60 * 60 * 1000) // Execute every 24 hours
    public void processAggregatedEmail() {
        if (!emails.isEmpty() && isReadyForProcessing()) {
            // final run
            emails.clear();
        }
    }*/
}
