package com.ureca.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Table(name = "ticketInfo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TicketInfoEntity {

    @Id
    @Column(name = "ticketId", length = 30, nullable = false)
    private String ticketId;

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
    private Date pymnDt;

    @ManyToOne
    @JoinColumn(name = "scrnnId", insertable = false, updatable = false)
    private ScreenInfoEntity scrnnId;

    @Builder
    public TicketInfoEntity(
            String ticketId
            , ScreenInfoEntity scrnnId
            , String userId
            , int seatNum
            , int pymnAmnt
            , String pymnInfo
            , Date pymnDt) {
        this.ticketId = ticketId;
        this.scrnnId = scrnnId;
        this.userId = userId;
        this.seatNum = seatNum;
        this.pymnAmnt = pymnAmnt;
        this.pymnInfo = pymnInfo;
        this.pymnDt = pymnDt;
    }

}
