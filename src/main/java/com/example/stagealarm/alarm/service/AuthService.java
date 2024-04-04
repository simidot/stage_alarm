package com.example.stagealarm.alarm.service;

import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
  void sendMail(String to, String subject, String text) throws MessagingException;
}
