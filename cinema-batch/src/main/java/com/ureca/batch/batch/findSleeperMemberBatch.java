package com.ureca.batch.batch;

import com.ureca.domain.entity.MemberEntity;
import com.ureca.domain.repository.MemberRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

@AllArgsConstructor
@Configuration
public class findSleeperMemberBatch {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final MemberRepository memberRepository;

    // 메모리에 저장할 리스트
    @Getter private List<MemberEntity> sleeperMembers = new ArrayList<>();

    @Bean
    public Job findSleeperMemberJob() {
        System.out.println("findSleeperMemberJob");
        return new JobBuilder("findSleeperMemberJob", jobRepository)
                .start(findSleeperMemberStep())
                .build();
    }

    @Bean
    public Step findSleeperMemberStep() {
        System.out.println("findSleeperMemberStep");
        return new StepBuilder("findSleeperMemberStep", jobRepository)
                .<MemberEntity, MemberEntity>chunk(10, platformTransactionManager)
                .reader(sleeperMemberReader())
                .processor(sleeperMemberProcessor())
                .writer(sleeperMemberWriter())
                .build();
    }

    @Bean
    public RepositoryItemReader<MemberEntity> sleeperMemberReader() {
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);

        return new RepositoryItemReaderBuilder<MemberEntity>()
                .name("sleeperMemberReader")
                .pageSize(10) // 페이지 단위로 데이터를 읽어옴
                .repository(memberRepository)
                .methodName("findSleeperMembers") // 적절한 메서드 이름으로 변경
                .arguments(oneYearAgo) // 메서드의 인자로 전달
                .sorts(Map.of("lastLoginTime", Sort.Direction.ASC)) // 정렬 기준
                .build();
    }

    @Bean
    public ItemProcessor<MemberEntity, MemberEntity> sleeperMemberProcessor() {
        return item -> {
            // 빌더 패턴을 사용하여 MemberEntity의 상태를 업데이트
            return MemberEntity.builder()
                    .id(item.getId())
                    .password(item.getPassword())
                    .name(item.getName())
                    .phone(item.getPhone())
                    .birth(item.getBirth())
                    .totalReservedTickets(item.getTotalReservedTickets())
                    .lastLoginTime(item.getLastLoginTime())
                    .acmltCnt(item.getAcmltCnt())
                    .code(item.getCode()) // 기존 등급 유지
                    .isInactive(true) // isInactive를 true로 설정
                    .build();
        };
    }

    @Bean
    public ItemWriter<MemberEntity> sleeperMemberWriter() {
        return items -> {
            // 메모리에 유저 데이터를 추가
            sleeperMembers.addAll(items.getItems());

            // 업데이트된 상태를 DB에 저장
            for (MemberEntity member : items) {
                memberRepository.save(member); // 업데이트
            }
        };
    }
}
