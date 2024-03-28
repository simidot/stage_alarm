package com.example.stagealarm.alarm.controller;

import com.example.stagealarm.alarm.dto.AlertDto;
import com.example.stagealarm.alarm.entity.Alert;
import com.example.stagealarm.alarm.repo.AlertRepository;
import com.example.stagealarm.alarm.service.EmailAlertService;
import com.example.stagealarm.genre.entity.GenreSubscribe;
import com.example.stagealarm.genre.repo.GenreSubscribeRepo;
import com.example.stagealarm.show.entity.ShowInfo;
import com.example.stagealarm.show.repo.ShowInfoRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AlertController {
    private final EmailAlertService alertService;
    private final AlertRepository alertRepository;
    private final GenreSubscribeRepo genreSubscribeRepo;
    private final ShowInfoRepository showInfoRepository;

    @PostMapping("/alert/send/{showInfoId}")
    public void sendAlert(@PathVariable("showInfoId") Long showInfoId) throws MessagingException {
        alertService.createAlert(showInfoId);
    }

}
