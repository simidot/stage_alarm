package com.example.stagealarm.alarm.service;

import com.example.stagealarm.alarm.repo.AlertRepository;
import com.example.stagealarm.genre.entity.GenreSubscribe;
import com.example.stagealarm.genre.repo.GenreSubscribeRepo;
import com.example.stagealarm.user.entity.UserEntity;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailAlertService implements AlertService {
    @Autowired
    private JavaMailSender mailSender;
    private final AlertRepository alertRepository;
    private final GenreSubscribeRepo genreSubscribeRepo;

    private void sendMail(
        String to,
        String subject,
        String text
    ) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("noreply@stagealarm.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);
        mailSender.send(message);
    }

    @Override
    public void sendGenreShowUpload(Long genreId, Long showInfoId) throws MessagingException {
        List<GenreSubscribe> subscribeList = genreSubscribeRepo.findByGenreId(genreId);
        for (GenreSubscribe list : subscribeList) {
            UserEntity foundUser = list.getUserEntity();
            String to = foundUser.getEmail();
            String text = foundUser.getLoginId()+"님, 구독하신 "+list.getGenre()+"장르의 공연 정보가 업데이트 되었습니다! \n\n"
                +"해당 링크 : \n\n\n" //todo: 해당 링크 가져와서 넣기
                +"저희 스테이지 알람 서비스를 이용해주셔서 감사합니다.";

            this.sendMail(to, "스테이지 알람", text);
        }
    }

    @Override
    public void sendArtistShowUpload(Long artistSubscribeId, Long showInfoId) {


    }

    @Override
    public void sendEmail() throws MessagingException {
        this.sendMail("sgi032151@gmail.com", "스테이지알람", "안녕하세요");
    }
}
