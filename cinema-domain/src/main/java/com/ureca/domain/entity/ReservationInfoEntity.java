package com.ureca.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 예매 정보
@Entity
@Table(name = "reservationInfo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationInfoEntity {

    // 예매 아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rsrvId")
    private Integer rsrvId;

    // 티켓 아이디
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticketId")
    private TicketInfoEntity ticketId;

    // 좌석 아이디
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seatId")
    private SeatInfoEntity seatId;

    @Builder
    public ReservationInfoEntity(Integer rsrvId, TicketInfoEntity ticketId, SeatInfoEntity seatId) {
        this.rsrvId = rsrvId;
        this.ticketId = ticketId;
        this.seatId = seatId;
    }
}
