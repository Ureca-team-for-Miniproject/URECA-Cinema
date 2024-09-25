package com.ureca.domain.repository;

import com.ureca.domain.entity.MemberEntity;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<MemberEntity, String> {
    Page<MemberEntity> findByTotalReservedTicketsGreaterThanEqual(
            Integer tickets, Pageable pageable);

    // 휴면회원 리스트 조회
    @Query(
            "select m from MemberEntity m where m.lastLoginTime < :oneYearAgo and m.isInactive = false")
    Page<MemberEntity> findSleeperMembers(
            @Param("oneYearAgo") LocalDate oneYearAgo, Pageable pageable);
}
