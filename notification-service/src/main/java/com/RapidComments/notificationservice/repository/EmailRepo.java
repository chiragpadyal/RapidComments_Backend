package com.RapidComments.notificationservice.repository;

import com.RapidComments.notificationservice.model.EmailModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmailRepo extends JpaRepository<EmailModel, UUID> {
    List<EmailModel> findByEmailIdAndIsRead(String emailId, boolean isRead);

    List<EmailModel> findByIsRead(boolean isRead);

}

