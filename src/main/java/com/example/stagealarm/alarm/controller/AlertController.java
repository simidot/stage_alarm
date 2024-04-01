package com.example.stagealarm.alarm.controller;

import com.example.stagealarm.alarm.dto.AlertDto;
import com.example.stagealarm.alarm.service.EmailAlertService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AlertController {
    private final EmailAlertService alertService;

    @PostMapping("/alert/send")
    public void sendAlarm() throws MessagingException {
        alertService.sendEmail();
    }
}
