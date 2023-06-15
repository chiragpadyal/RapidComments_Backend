package com.RapidComments.notificationservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class EmailModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

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
