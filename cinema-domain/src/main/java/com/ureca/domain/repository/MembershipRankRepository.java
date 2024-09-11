package com.ureca.domain.repository;

import com.ureca.domain.entity.MembershipRankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipRankRepository extends JpaRepository<MembershipRankEntity, Integer> {}
