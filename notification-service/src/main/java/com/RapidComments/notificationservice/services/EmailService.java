package com.RapidComments.notificationservice.services;

import com.RapidComments.notificationservice.model.EmailModel;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.ui.freemarker.SpringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;


    private final OkHttpClient client;

    @Value("${mail-trap.api-key}")
    private String apikey;

    @Value("${mail-trap.from}")
    private String from;

    @Autowired
    private Configuration config;


    public EmailService() {
        this.client = new OkHttpClient().newBuilder().build();
    }


    public void sendEmailMailTrap(EmailModel emailModel) throws IOException {

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,
                "{\"from\":{\"email\":\"" + from + "\",\"name\":\"Mailtrap Test\"},\"to\":[{\"email\":\"" + emailModel.getEmailId() + "\"}],\"subject\":\"" + emailModel.getMessageHeader() + "\",\"text\":\"" + emailModel.getMessageBody() + "\",\"category\":\"" + "Integration Test" + "\"}");
        Request request = new Request.Builder()
                .url("https://send.api.mailtrap.io/api/send")
                .method("POST", body)
                .addHeader("Authorization", String.format("Bearer %s", apikey))
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        log.info(response.message());
    }


    public void sendEmail(EmailModel[][] emailModels) throws MessagingException, IOException, TemplateException {
        MimeMessage message = mailSender.createMimeMessage();

        message.setFrom(new InternetAddress(from));
        message.setRecipient(Message.RecipientType.TO,
                new InternetAddress(emailModels[0][0].getEmailId()));

        message.setSubject(emailModels[0][0].getMessageHeader());

        // message body
        Template t = config.getTemplate("mail.ftl");
        Map<String, Object> model = new HashMap<>();
        model.put("thread", emailModels);
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
        message.setContent(html, "text/html; charset=utf-8");

        mailSender.send(message);

    }
}