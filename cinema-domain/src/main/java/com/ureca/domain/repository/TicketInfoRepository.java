package com.ureca.domain.repository;

import com.ureca.domain.dto.ResTicketDTO;
import com.ureca.domain.dto.SeatDTO;
import com.ureca.domain.entity.TicketInfoEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketInfoRepository extends JpaRepository<TicketInfoEntity, Integer> {
    // 사용자 ID로 최다 예매 장르 상위 3개 조회
    @Query(
            value =
                    "SELECT mi.genreNm, count(*) FROM ticketInfo ti "
                            + "JOIN screenInfo si ON ti.scrnnId = si.scrnnId "
                            + "JOIN movieInfo mi ON si.movieId = mi.movieId "
                            + "WHERE ti.userId = :userId "
                            + "GROUP BY mi.genreNm",
            nativeQuery = true)
    List<Object[]> findGenresByUserId(@Param("userId") String userId);

    // 발행된 티켓 정보 조회
    @Query(
            "SELECT new com.ureca.domain.dto.ResTicketDTO("
                    + "c.ticketId, "
                    + "b.movieImgUrl, "
                    + "b.movieNm, "
                    + "b.rtngRstrCd, "
                    + "a.startScrnn, "
                    + "d.theaterNm, "
                    + "c.seatNum, "
                    + "(CASE WHEN a.scrnnDate > CURRENT_DATE THEN 'Y' ELSE 'N' END), "
                    + "c.pymnDt, "
                    + "c.pymnInfo, "
                    + "c.pymnAmnt) "
                    + "FROM TicketInfoEntity c "
                    + "JOIN c.scrnnId a "
                    + "JOIN a.movieId b "
                    + "JOIN a.theaterId d "
                    + "WHERE c.ticketId = :ticketId")
    ResTicketDTO findTicketDetailsByTicketId(@Param("ticketId") int ticketId);

    // 목록 조회 - 좌석 정보 조회
    @Query(
            "SELECT new com.ureca.domain.dto.SeatDTO(b.seatId, b.seatRow, b.seatCol, b.seatType, "
                    + "(SELECT ae.seatAmount FROM AmountInfoEntity ae WHERE ae.seatType = b.seatType), "
                    + "(CASE WHEN EXISTS(SELECT 1 FROM ReservationInfoEntity r WHERE r.seatId = b) THEN 'Y' ELSE 'N' END)) "
                    + "FROM ReservationInfoEntity a "
                    + "JOIN SeatInfoEntity b ON a.seatId = b "
                    + "JOIN TicketInfoEntity c ON a.ticketId = c "
                    + "WHERE c.ticketId = :ticketId")
    List<SeatDTO> findTicketList(@Param("ticketId") int ticketId);
}
