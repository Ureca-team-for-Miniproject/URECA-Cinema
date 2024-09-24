package com.ureca.domain.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 티켓 정보
@Entity
@Table(name = "ticketInfo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TicketInfoEntity {

    // 티켓 아이디 (예매번호)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticketId")
    private int ticketId;

    // 사용자 아이디
    @Column(name = "userId", length = 20, nullable = false)
    private String userId;

    // 인원수
    @Column(name = "seatNum")
    private int seatNum;

    // 결제 금액
    @Column(name = "pymnAmnt")
    private int pymnAmnt;

    // 결제 정보
    @Column(name = "pymnInfo", length = 30)
    private String pymnInfo;

    // 예매 완료 시각(YYYY-MM-DD HH:MI:SS)
    @Column(name = "pymnDt")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp pymnDt;

    // 상영 아이디
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scrnnId")
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
