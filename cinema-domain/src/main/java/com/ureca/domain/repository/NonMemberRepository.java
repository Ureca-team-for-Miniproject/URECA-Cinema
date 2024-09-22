package com.ureca.domain.repository;

import com.ureca.domain.entity.NonMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NonMemberRepository extends JpaRepository<NonMemberEntity, String> {}
