package com.ureca.domain.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.antlr.v4.runtime.misc.NotNull;

// 좌석 조회 화면 응답 DTO
@Data
@Getter
public class ResSeatDTO {

    // 영화명
    @NotNull private String movieNm;
    // 상영관명
    @NotNull private String theaterNm;
    // 상영관 아이디
    @NotNull private String theaterId;
    // 상영 아이디
    @NotNull private String scrnnId;
    // 상영방식
    @NotNull private String theaterCd;
    // 사용자 적립금
    @NotNull private int useAcmltCnt;
    // 좌석 목록
    @NotNull private List<SeatDTO> seatList = new ArrayList<>();
    // 전체 좌석 수
    @NotNull private int totalSeatCnt;
    // 행 개수
    @NotNull private int rowSeatCnt;
    // 열 개수 (12고정)
    @NotNull private int colSeatCnt;

    @Builder
    public ResSeatDTO(
            String movieNm,
            String theaterNm,
            String theaterCd,
            String theaterId,
            String scrnnId,
            int useAcmltCnt) {
        this.movieNm = movieNm;
        this.theaterCd = theaterCd;
        this.theaterNm = theaterNm;
        this.theaterId = theaterId;
        this.scrnnId = scrnnId;
        this.useAcmltCnt = useAcmltCnt;
    }
}
