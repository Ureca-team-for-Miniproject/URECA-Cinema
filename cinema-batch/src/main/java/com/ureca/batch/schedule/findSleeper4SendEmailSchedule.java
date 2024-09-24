package com.ureca.batch.schedule;

import com.ureca.batch.batch.findSleeperMemberBatch;
import com.ureca.domain.entity.MemberEntity;
import com.ureca.domain.entity.MovieInfoEntity;
import com.ureca.domain.service.MovieRecommendService;
import com.ureca.external.service.EmailService;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@AllArgsConstructor
@Configuration
public class findSleeper4SendEmailSchedule {
    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;
    private final EmailService emailService;
    private final findSleeperMemberBatch findSleeperMemberBatch;
    private final MovieRecommendService movieRecommendService;

    @Scheduled(cron = "10 * * * * ?", zone = "Asia/Seoul") // 매일 오전 10시에 실행
    public void sendEmailToSleeperMembers() throws Exception {
        System.out.println("updateMemberRank schedule start");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        String date = dateFormat.format(new Date());

        JobParameters jobParameters =
                new JobParametersBuilder().addString("date", date).toJobParameters();
        jobLauncher.run(jobRegistry.getJob("findSleeperMemberJob"), jobParameters);

        // 메모리에 저장된 슬리퍼 멤버 가져오기
        List<MemberEntity> sleeperMembers = findSleeperMemberBatch.getSleeperMembers();
        if (sleeperMembers != null && !sleeperMembers.isEmpty()) {
            // 추천 영화 목록을 가져옴
            Map<MemberEntity, List<MovieInfoEntity>> memberMovieMap =
                    movieRecommendService.recommendAlgo(sleeperMembers);
            // 이메일 발송
            emailService.sendEmail(memberMovieMap);
            System.out.println("findSleeper4SendEmailSchedule 이메일이 보내졌습니다.");
        }
    }
}
