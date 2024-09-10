package com.ureca.domain.repository;

import com.ureca.domain.entity.MembershipRank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipRankRepository extends JpaRepository<MembershipRank, Integer> {
}
