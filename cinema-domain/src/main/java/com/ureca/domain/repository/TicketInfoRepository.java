package com.ureca.domain.repository;

import com.ureca.domain.entity.TicketInfoEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketInfoRepository extends JpaRepository<TicketInfoEntity, String> {
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
}
