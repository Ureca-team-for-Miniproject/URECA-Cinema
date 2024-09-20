package com.ureca.domain.repository;

import com.ureca.domain.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, String> {
    Page<MemberEntity> findByTotalReservedTicketsGreaterThanEqual(
            Integer tickets, Pageable pageable);
}
