package com.ureca.domain.repository;

import com.ureca.domain.entity.ReservationInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationInfoRepository extends JpaRepository<ReservationInfoEntity, Integer> {
}
