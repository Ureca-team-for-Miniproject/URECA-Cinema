package com.ureca.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "seatInfo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeatInfoEntity {

    @Id
    @Column(name = "seatId", length = 14, nullable = false)
    private String seatId;

    @Column(name = "seatRow", length = 8)
    private String seatRow;

    @Column(name = "seatCol", length = 8)
    private String seatCol;

    @Column(name = "seatType", length = 10)
    @ColumnDefault("S")
    private String seatType;

    @Column(name = "rsrvYn", length = 4)
    @ColumnDefault("N")
    private String rsrvYn;

    @Column(name = "selectYn", length = 4)
    @ColumnDefault("Y")
    private String selectYn;

    @ManyToOne
    @JoinColumn(name = "theaterId", insertable = false, updatable = false)
    private TheaterInfoEntity theaterId;

    @Builder
    public SeatInfoEntity(
            String seatId,
            TheaterInfoEntity theaterId,
            String seatRow,
            String seatCol,
            String seatType,
            String rsrvYn,
            String selectYn) {
        this.seatId = seatId;
        this.theaterId = theaterId;
        this.seatRow = seatRow;
        this.seatCol = seatCol;
        this.seatType = seatType;
        this.rsrvYn = rsrvYn;
        this.selectYn = selectYn;
    }
}
