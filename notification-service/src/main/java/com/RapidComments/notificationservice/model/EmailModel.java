package com.RapidComments.notificationservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class EmailModel {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "BINARY(16)", nullable = false, updatable = false)
    private UUID id;

    @Email
    @NotEmpty
    private String emailId;
    private String recipientName;
    private String messageHeader;
    private String messageBody;

    private String replyBody ;

    /**
     * Notification is readed or not
     */
    private boolean isRead = false;

    /**
     * Thread Website name
     */
    private String threadName;
    /**
     * Post name on given website
     */
    private String subThreadName;
}
