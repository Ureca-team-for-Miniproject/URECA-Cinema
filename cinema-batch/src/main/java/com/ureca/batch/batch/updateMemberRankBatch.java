package com.ureca.batch.batch;

import com.ureca.domain.entity.MemberEntity;
import com.ureca.domain.entity.MembershipRankEntity;
import com.ureca.domain.repository.MemberRepository;
import com.ureca.domain.repository.MembershipRankRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;
import java.util.Map;

@Configuration
public class updateMemberRankBatch {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final MemberRepository memberRepository;
    private final MembershipRankRepository membershipRankRepository;


    public updateMemberRankBatch(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, MemberRepository memberRepository, MembershipRankRepository membershipRankRepository) {

        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.memberRepository = memberRepository;
        this.membershipRankRepository = membershipRankRepository;
    }

    @Bean
    // Job을 정의
    public Job firstJob() {

        System.out.println("updateMemberRankJob");

        return new JobBuilder("updateMemberRankJob", jobRepository)
                .start(firstStep()) //스텝을 정의할 수 있음 1개 이상일 경우 .next()
                .build();
    }

    @Bean
    public Step firstStep() {

        System.out.println("updateMemberRankStep");

        return new StepBuilder("updateMemberRankStep", jobRepository)
                .<MemberEntity, MemberEntity> chunk(10, platformTransactionManager)
                .reader(memberRankReader())
                .processor(memberRankProcessor())
                .writer(memberRankWriter())
                .build();
    }

    @Bean
    public RepositoryItemReader<MemberEntity> memberRankReader() {

        return new RepositoryItemReaderBuilder<MemberEntity>()
                .name("memberRankReader")
                .pageSize(10)  // 페이지 단위로 데이터를 읽어옴
                .methodName("findAll")  // findAll로 모든 데이터를 읽어옴
                .repository(memberRepository)
                .sorts(Map.of("totalReservedTickets", Sort.Direction.ASC))  // 정렬 기준
                .build();
    }


    @Bean
    public ItemProcessor<MemberEntity, MemberEntity> memberRankProcessor() {
        return item -> {
            MembershipRankEntity rankEntity;

            if (item.getTotalReservedTickets() >= 10) {
                rankEntity = membershipRankRepository.findByCode(4);  // MVP 등급
            } else if (item.getTotalReservedTickets() >= 6) {
                rankEntity = membershipRankRepository.findByCode(3);  // 골드 등급
            } else if (item.getTotalReservedTickets() >= 3) {
                rankEntity = membershipRankRepository.findByCode(2);  // 실버 등급
            } else {
                rankEntity = membershipRankRepository.findByCode(1);  // 기본 등급
            }

            return MemberEntity.builder()
                    .id(item.getId())
                    .password(item.getPassword())
                    .name(item.getName())
                    .phone(item.getPhone())
                    .birth(item.getBirth())
                    .totalReservedTickets(item.getTotalReservedTickets())
                    .lastLoginTime(item.getLastLoginTime())
                    .acmltCnt(item.getAcmltCnt())
                    .code(rankEntity)  // 등급 업데이트
                    .build();
        };
    }

    @Bean
    public RepositoryItemWriter<MemberEntity> memberRankWriter() {

        return new RepositoryItemWriterBuilder<MemberEntity>()
                .repository(memberRepository)
                .methodName("save")
                .build();
    }

}