package com.ureca.domain.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

// 좌석 선택 완료 정보 DTO
@Getter
@Setter
public class SeatSaveDTO {

    // 상영 아이디
    @NotNull private String scrnnId;
    // 상영관 아이디
    @NotNull private String theaterId;
    // 예매자 아이디
    @NotNull private String userId;
    // 인원수
    @NotNull private int seatNum;
    // 사용적립금
    private int useAcmltCnt;
    // 결제정보
    @NotNull private String pymnInfo;
    // 결제금액
    @NotNull private int pymnAmnt;
    // 예매 좌석 목록 (좌석 아이디만 있음 SeatDTO-seatId)
    @NotNull private List<SeatDTO> seatList;

    // 기본 생성자
    public SeatSaveDTO() {}

    // 생성자
    public SeatSaveDTO(
            String scrnnId,
            String theaterId,
            String userId,
            int seatNum,
            String pymnInfo,
            int pymnAmnt) {
        this.scrnnId = scrnnId;
        this.theaterId = theaterId;
        this.userId = userId;
        this.seatNum = seatNum;
        this.pymnInfo = pymnInfo;
        this.pymnAmnt = pymnAmnt;
    }
}
