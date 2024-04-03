package com.example.stagealarm.alarm.service;

import com.example.stagealarm.alarm.entity.Alert;
import com.example.stagealarm.alarm.repo.AlertRepository;
import com.example.stagealarm.artist.entity.ArtistSubscribe;
import com.example.stagealarm.artist.repo.ArtistSubscribeRepo;
import com.example.stagealarm.genre.entity.GenreSubscribe;
import com.example.stagealarm.genre.repo.GenreSubscribeRepo;
import com.example.stagealarm.show.entity.ShowArtist;
import com.example.stagealarm.show.entity.ShowGenre;
import com.example.stagealarm.show.entity.ShowInfo;
import com.example.stagealarm.show.repo.ShowArtistRepo;
import com.example.stagealarm.show.repo.ShowGenreRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import javax.swing.text.html.Option;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private final ArtistSubscribeRepo artistSubscribeRepo;

    private static final String EMAIL_TITLE_PREFIX = "[STAGE ALARM] 알림 : 새 공연이 등록되었습니다";
    private static final String EMAIL_ALARM_TITLE = "알림 : 새 공연이 등록되었습니다";

    @Override
    @Async("threadPoolTaskExecutor")
    public void createAlert(Long showInfoId) {
        // 해당 공연정보에서 아티스트 관련 알림 객체 생성
        log.info("===== artist alert creation start");
        List<ShowArtist> showArtists = showArtistRepo.findByShowInfoId(showInfoId);
        generateArtistSubAlert(showArtists);
        log.info("===== artist alert creation end");

        log.info("===== genre alert creation start");
        List<ShowGenre> showGenres = showGenreRepo.findByShowInfoId(showInfoId);
        generateGenreSubAlert(showGenres);
        log.info("===== genre alert creation end");

        List<Alert> alerts = alertRepository.findByShowInfoId(showInfoId);
        for (Alert alert : alerts) {
            try {
                log.info("send email start");
                alert.setMessage(generateMessage(alert, alert.getUserNickname()));
                sendMail(alert);
            } catch (MessagingException e) {
                log.warn(e.getMessage());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    private void generateArtistSubAlert(List<ShowArtist> shows) {
        for (ShowArtist artist : shows) {
            List<ArtistSubscribe> subscribes = artistSubscribeRepo.findByArtistId(artist.getArtist().getId());
            for(ArtistSubscribe subscribe : subscribes) {
                Alert alert = Alert.builder()
                    .showInfo(artist.getShowInfo())
                    .userEmail(subscribe.getUserEntity().getEmail())
                    .userNickname(subscribe.getUserEntity().getNickname())
                    .artistSubscribe(subscribe)
                    .title(EMAIL_ALARM_TITLE)
                    .build();

                alert = alertRepository.save(alert);
                log.info("saved..artist alert : "+alert.toString());
            }
        }
    }

    private void generateGenreSubAlert(List<ShowGenre> shows) {
        for (ShowGenre genre : shows) {
            List<GenreSubscribe> subscribes = genreSubscribeRepo.findByGenreId(genre.getGenre().getId());
            for (GenreSubscribe subscribe : subscribes) {
                String userEmail = subscribe.getUserEntity().getEmail();
                Optional<Alert> alertOptional = alertRepository.findByUserEmailAndShowInfoId(userEmail, genre.getShowInfo().getId());
                Alert alert;
                // 이미 해당 구독자 유저에 대한 알림이 생성된 상태이면 이미 생성된 alert에 추가만 하고 알림 이메일은 보내지 않음
                if (alertOptional.isPresent()) {
                    alert = alertOptional.get();
                    alert.setGenreSubscribe(subscribe);
                    alertRepository.save(alert);
                    log.info("==== set same alert for " + subscribe.getUserEntity().getNickname());
                    continue;
                } else { // 한 유저에 대한 알림이 생성되지 않은 상태이면 새로 생성
                    alert = Alert.builder()
                        .showInfo(genre.getShowInfo())
                        .genreSubscribe(subscribe)
                        .title(EMAIL_ALARM_TITLE)
                        .userEmail(userEmail)
                        .userNickname(subscribe.getUserEntity().getNickname())
                        .build();
                    alertRepository.save(alert);
                }
                log.info("saved..genre alert : "+alert.toString());
            }
        }
    }

    private String generateMessage(Alert alert, String userNickname){
        StringBuffer sb = new StringBuffer();
        sb.append("안녕하세요. ").append(userNickname).append("님, 스테이지 알람에서 알림 드립니다.   \n");
        sb.append("구독하신 ");
        if (alert.getGenreSubscribe() != null && alert.getArtistSubscribe()!=null) {
            sb.append("아티스트 :: ").append(alert.getArtistSubscribe().getArtist().getName()).append("와 ");
            sb.append("장르 :: ").append(alert.getGenreSubscribe().getGenre().getName()).append("의 공연 정보가 등록되었습니다.  \n");
        } else if (alert.getGenreSubscribe() == null) {
            sb.append("아티스트 :: ").append(alert.getArtistSubscribe().getArtist().getName()).append("의 공연 정보가 등록되었습니다.  \n");
        } else {
            sb.append("장르 :: ").append(alert.getGenreSubscribe().getGenre().getName()).append("의 공연 정보가 등록되었습니다.  \n");
        }
        sb.append("해당 공연 보기 : ").append(alert.getShowInfo().getTicketVendor()).append(" \n\n");
        sb.append("저희 스테이지 알람을 사랑해주셔서 감사합니다. ");

        return sb.toString();
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public void sendMail(Alert alert) throws MessagingException {
        log.info("===== email sending start");

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setSubject(EMAIL_TITLE_PREFIX + alert.getTitle()); //제목
        helper.setFrom("stage alarm <noreply@stagealarm.com>");
        helper.setTo(alert.getUserEmail());
        HashMap<String, String> emailValues = new HashMap<>();
        emailValues.put("content", alert.getMessage());
        String text = setContext(emailValues);
        helper.setText(text, true);
        helper.addInline("logo", new ClassPathResource("static/images/logo.png"));
        helper.addInline("notice-icon", new ClassPathResource("static/images/image-1.png"));

        mailSender.send(message);
        log.info("===== email sending end");
    }

    private String setContext(Map<String, String> emailValues) {
        Context context = new Context();
        emailValues.forEach(context::setVariable);
        return templateEngine.process("email/index.html", context);
    }
}
