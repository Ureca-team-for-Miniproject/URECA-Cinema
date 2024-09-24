package com.ureca.domain.repository;

import com.ureca.domain.entity.ReservationInfoEntity;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationInfoRepository extends JpaRepository<ReservationInfoEntity, Integer> {

    // 예매된 좌석인지 확인 1:예매됨 2:예매안됨
    @Query(
            "SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM ReservationInfoEntity r WHERE r.seatId.seatId = :seatId")
    boolean existsBySeatId(@Param("seatId") String seatId);

    /* 예매
    findById 메서드에서 PESSIMISTIC_WRITE 락을 적용하면
    해당 엔티티에 대한 비관적 락이 설정되어
    다른 트랜잭션이 해당 데이터에 대한 쓰기 작업을 할 수 없게 됩니다. */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ReservationInfoEntity> findById(Integer id);
}
