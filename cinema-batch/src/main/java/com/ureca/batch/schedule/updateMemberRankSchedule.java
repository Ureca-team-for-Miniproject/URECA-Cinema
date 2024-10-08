package com.ureca.batch.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@AllArgsConstructor
@Configuration
public class updateMemberRankSchedule {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    @Scheduled(cron = "0 0 0 1 * ?", zone = "Asia/Seoul")
    public void runUpdateMemberRankJob() throws Exception {

        System.out.println("updateMemberRank schedule start");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        String date = dateFormat.format(new Date());

        JobParameters jobParameters =
                new JobParametersBuilder().addString("date", date).toJobParameters();

        jobLauncher.run(jobRegistry.getJob("updateMemberRankJob"), jobParameters);
    }
}
