package com.ureca.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

// 좌석 DTO
@Getter
@Setter
public class SeatDTO {
    // 좌석 아이디
    @NotNull private String seatId;
    // 좌석 코드(행)
    @NotNull private String seatRow;
    // 좌석번호(열)
    @NotNull private int seatCol;
    // 좌석 유형(S:일반석, R:장애인석, C:커플석)
    @NotNull private String seatType;
    // 좌석 금액
    @NotNull private int seatAmount;
    // 예매 완료 여부
    @NotNull private String rsrvYn;

    // 생성자
    public SeatDTO(
            String seatId,
            String seatRow,
            int seatCol,
            String seatType,
            int seatAmount,
            String rsrvYn) {
        this.seatId = seatId;
        this.seatRow = seatRow;
        this.seatCol = seatCol;
        this.seatType = seatType;
        this.seatAmount = seatAmount;
        this.rsrvYn = rsrvYn;
    }
}
