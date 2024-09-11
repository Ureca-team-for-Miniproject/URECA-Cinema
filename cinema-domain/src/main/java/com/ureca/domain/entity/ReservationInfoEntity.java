package com.ureca.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservationInfo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rsrvId")
    private Integer rsrvId;

    @ManyToOne
    @JoinColumn(name = "ticketId", insertable = false, updatable = false)
    private TicketInfoEntity ticketId;

    @ManyToOne
    @JoinColumn(name = "seatId", insertable = false, updatable = false)
    private SeatInfoEntity seatId;

    @Builder
    public ReservationInfoEntity(Integer rsrvId, TicketInfoEntity ticketId, SeatInfoEntity seatId) {
        this.rsrvId = rsrvId;
        this.ticketId = ticketId;
        this.seatId = seatId;
    }
}
