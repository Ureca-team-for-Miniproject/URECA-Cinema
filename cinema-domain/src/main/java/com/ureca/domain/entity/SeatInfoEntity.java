package com.ureca.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

// 좌석 정보
@Entity
@Table(name = "seatInfo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeatInfoEntity {

    // 좌석 아이디
    @Id
    @Column(name = "seatId", length = 14, nullable = false)
    private String seatId;

    // 좌석 코드(행)
    @Column(name = "seatRow", length = 8)
    private String seatRow;

    // 좌석 번호(열)
    @Column(name = "seatCol")
    private int seatCol;

    // 좌석 유형
    @Column(name = "seatType", length = 10)
    @ColumnDefault("S")
    private String seatType;

    // 상영관 아이디
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theaterId")
    private TheaterInfoEntity theaterId;

    @Builder
    public SeatInfoEntity(
            String seatId,
            TheaterInfoEntity theaterId,
            String seatRow,
            int seatCol,
            String seatType) {
        this.seatId = seatId;
        this.theaterId = theaterId;
        this.seatRow = seatRow;
        this.seatCol = seatCol;
        this.seatType = seatType;
    }
}
