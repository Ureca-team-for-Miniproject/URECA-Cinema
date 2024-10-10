package com.ureca.domain.repository;

import com.ureca.domain.dto.ResSeatDTO;
import com.ureca.domain.dto.SeatDTO;
import com.ureca.domain.entity.SeatInfoEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatInfoRepository extends JpaRepository<SeatInfoEntity, String> {

    // 단건 조회 - 상영 정보 조회
    @Query(
            "SELECT new com.ureca.domain.dto.ResSeatDTO(b.movieNm, c.theaterNm, c.theaterCd, "
                    + "a.scrnnId, c.theaterId, "
                    + "(SELECT m.acmltCnt FROM MemberEntity m WHERE m.id = :userId)) "
                    + "FROM ScreenInfoEntity a "
                    + "JOIN a.movieId b " // MovieInfoEntity (ScreenInfoEntity-movieId)
                    + "JOIN a.theaterId c " // TheaterInfoEntity (ScreenInfoEntity-theaterId)
                    + "WHERE a.scrnnId = :scrnnId "
                    + "AND b.movieId = :movieId "
                    + "AND c.theaterId = :theaterId")
    ResSeatDTO findSeatInfo(
            @Param("userId") String userId,
            @Param("scrnnId") String scrnnId,
            @Param("movieId") String movieId,
            @Param("theaterId") String theaterId);

    // 목록 조회 - 좌석 정보 조회
    @Query(
            "SELECT new com.ureca.domain.dto.SeatDTO(b.seatId, b.seatRow, b.seatCol, b.seatType, "
                    + "(SELECT a.seatAmount FROM AmountInfoEntity a WHERE a.seatType = b.seatType), "
                    + "(CASE WHEN EXISTS(SELECT 1 FROM ReservationInfoEntity r WHERE r.seatId.seatId = b.seatId) THEN 'Y' ELSE 'N' END)) "
                    + "FROM ScreenInfoEntity a "
                    + "JOIN a.theaterId t "
                    + // t는 TheaterInfoEntity
                    "JOIN SeatInfoEntity b ON b.theaterId = t "
                    + // b와 t의 theaterId를 연결
                    "WHERE a.scrnnId = :scrnnId")
    List<SeatDTO> findSeatList(@Param("scrnnId") String scrnnId);
}
