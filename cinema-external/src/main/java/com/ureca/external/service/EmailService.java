package com.ureca.external.service;

import com.ureca.domain.entity.MemberEntity;
import com.ureca.domain.entity.MovieInfoEntity;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.List;
import java.util.Map;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailService {

    private JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    // 메일 발송 로직
    public void sendEmail(Map<MemberEntity, List<MovieInfoEntity>> memberMovieMap) {
        for (Map.Entry<MemberEntity, List<MovieInfoEntity>> entry : memberMovieMap.entrySet()) {
            sendHtmlEmail(entry.getKey().getId(), entry.getValue());
        }
    }

    // HTML 발송 로직
    public void sendHtmlEmail(String to, List<MovieInfoEntity> movieInfoEntities) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            // 메일 수신인
            helper.setTo(to);
            // 메일 제목
            helper.setSubject("[URECA CINEMA] 취향에 맞는 영화를 가져왔어요 ✨");
            // HTML (영화내용) 설정
            Context context = new Context();
            context.setVariable("MovieList", movieInfoEntities);
            String html = templateEngine.process("email", context);
            helper.setText(html, true);
            // 메일 발송
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.out.println("메일 발송 실패: " + e.getMessage());
        }
    }
}
