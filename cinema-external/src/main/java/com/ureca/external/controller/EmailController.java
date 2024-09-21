package com.ureca.external.controller;

import com.ureca.domain.entity.MemberEntity;
import com.ureca.domain.entity.MovieInfoEntity;
import com.ureca.domain.service.MovieRecommendService;
import com.ureca.external.service.EmailService;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/mail")
@Controller
public class EmailController {

    private MovieRecommendService movieRecommendService;
    private EmailService emailService;

    public EmailController(MovieRecommendService movieRecommendService, EmailService emailService) {
        this.movieRecommendService = movieRecommendService;
        this.emailService = emailService;
    }

    // 휴면 회원 메일 발송 : http://localhost:8080/mail/send
    @PostMapping("/send")
    public ResponseEntity<String> sendToSleepy() {
        // <휴면회원,추천영화목록> 맵 가져오기
        Map<MemberEntity, List<MovieInfoEntity>> memberMovieMap =
                movieRecommendService.recommendAlgo();
        // 메일 발송
        emailService.sendEmail(memberMovieMap);
        return ResponseEntity.ok("메일이 성공적으로 발송되었습니다.");
    }
}
