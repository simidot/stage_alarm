package com.example.stagealarm.alarm.service;

import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

@Service
public interface AlertService {
    void sendGenreShowUpload(Long genreSubscribeId, Long showInfoId) throws MessagingException;

    void sendArtistShowUpload(Long artistSubscribeId, Long showInfoId);

    void sendEmail() throws MessagingException;
}
