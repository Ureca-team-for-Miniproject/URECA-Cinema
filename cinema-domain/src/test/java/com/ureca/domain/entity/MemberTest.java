package com.ureca.domain.entity;

import com.ureca.domain.repository.MemberRepository;
import com.ureca.domain.repository.MembershipRankRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberTest {

    @Autowired
    private MembershipRankRepository membershipRankRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void test() {
        MembershipRankEntity membershipRank = MembershipRankEntity.builder()
                .code(4)
                .rank("VIP")
                .acmltRate(0.1)
                .build();

        membershipRankRepository.save(membershipRank); // 먼저 저장

        MemberEntity member = MemberEntity.builder()
                .id("testUser123")
                .password("password123")
                .name("John Doe")
                .phone("010-1234-5678")
                .birth(new Date())
                .totalReservedTickets(10)
                .acmltCnt(5)
                .code(membershipRank)
                .build();

        // When: Member 저장
        memberRepository.save(member);

        // Then: 저장한 엔티티를 조회하고 값이 올바른지 확인
        MemberEntity foundMember = memberRepository.findById("testUser123").orElse(null);
        assertNotNull(foundMember); // 조회한 값이 null이 아님을 확인
        assertEquals("testUser123", foundMember.getId());
        assertEquals("John Doe", foundMember.getName());

        // 출력 확인
        System.out.println("===================================");
        System.out.println(foundMember);
    }
}