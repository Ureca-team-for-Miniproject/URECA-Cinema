package com.ureca.domain.repository;

import com.ureca.domain.entity.ReservationInfoEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationInfoRepository extends JpaRepository<ReservationInfoEntity, Integer> {

    // 예매된 좌석인지 확인 1:예매됨 2:예매안됨
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(
            "SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM ReservationInfoEntity r WHERE r.seatId.seatId = :seatId")
    boolean existsBySeatId(@Param("seatId") String seatId);
}
