package com.example.stagealarm.alarm.service;

import com.example.stagealarm.alarm.entity.Alert;
import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
public interface AlertService {
    void createAlert(Long showInfoId);
    void sendMail(Alert alert) throws MessagingException;
}
