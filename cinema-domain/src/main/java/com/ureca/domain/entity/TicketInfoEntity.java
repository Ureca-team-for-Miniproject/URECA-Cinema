package com.ureca.domain.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ticketInfo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TicketInfoEntity {

    @Id
    @Column(name = "ticketId", length = 30, nullable = false)
    private int ticketId;

    @Column(name = "userId", length = 20, nullable = false)
    private String userId;

    @Column(name = "seatNum")
    private int seatNum;

    @Column(name = "pymnAmnt")
    private int pymnAmnt;

    @Column(name = "pymnInfo", length = 30)
    private String pymnInfo;

    @Column(name = "pymnDt")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp pymnDt;

    @ManyToOne
    @JoinColumn(name = "scrnnId", insertable = false, updatable = false)
    private ScreenInfoEntity scrnnId;

    @Builder
    public TicketInfoEntity(
            int ticketId,
            ScreenInfoEntity scrnnId,
            String userId,
            int seatNum,
            int pymnAmnt,
            String pymnInfo,
            Timestamp pymnDt) {
        this.ticketId = ticketId;
        this.scrnnId = scrnnId;
        this.userId = userId;
        this.seatNum = seatNum;
        this.pymnAmnt = pymnAmnt;
        this.pymnInfo = pymnInfo;
        this.pymnDt = pymnDt;
    }
}
