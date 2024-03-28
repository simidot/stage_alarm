package com.example.stagealarm.alarm.service;

import com.example.stagealarm.alarm.entity.Alert;
import com.example.stagealarm.alarm.repo.AlertRepository;
import com.example.stagealarm.genre.entity.GenreSubscribe;
import com.example.stagealarm.genre.repo.GenreSubscribeRepo;
import com.example.stagealarm.show.entity.ShowGenre;
import com.example.stagealarm.show.repo.ShowArtistRepo;
import com.example.stagealarm.show.repo.ShowGenreRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailAlertService implements AlertService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    private final AlertRepository alertRepository;
    private final ShowArtistRepo showArtistRepo;
    private final ShowGenreRepo showGenreRepo;
    private final GenreSubscribeRepo genreSubscribeRepo;
    // todo: ArtistSubscribeRepo 추가

    private static final String EMAIL_TITLE_PREFIX = "[STAGE ALARM] ";

    @Override
    public void createAlert(Long showInfoId) {
        List<ShowGenre> showGenres = showGenreRepo.findByShowInfoId(showInfoId);
        for (ShowGenre genre : showGenres) {
            List<GenreSubscribe> subscribes = genreSubscribeRepo.findByGenreId(genre.getGenre().getId());
            for (GenreSubscribe subscribe : subscribes) {
                Alert alert = Alert.builder()
                    .genreSubscribe(subscribe)
                    .title("title")
                    .message("message")
                    .build();
                alert = alertRepository.save(alert);
                try {
                    this.sendMail(alert);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public void sendMail(Alert alert) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setSubject(EMAIL_TITLE_PREFIX + alert.getTitle()); //제목
        helper.setFrom("stage alarm <noreply@stagealarm.com>");
        helper.setTo(alert.getGenreSubscribe().getUserEntity().getEmail());
        HashMap<String, String> emailValues = new HashMap<>();
        emailValues.put("content", alert.getMessage());
        String text = setContext(emailValues);
        helper.setText(text, true);
        helper.addInline("logo", new ClassPathResource("static/images/logo.png"));
        helper.addInline("notice-icon", new ClassPathResource("static/images/image-1.png"));

        mailSender.send(message);
    }

    private String setContext(Map<String, String> emailValues) {
        Context context = new Context();
        emailValues.forEach(context::setVariable);
        return templateEngine.process("email/index.html", context);
    }
}
