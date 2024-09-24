package com.ureca.domain.entity;

import jakarta.persistence.*;
import java.sql.Date;
import java.sql.Time;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 상영 정보
@Entity
@Table(name = "screenInfo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScreenInfoEntity {

    // 상영 아이디
    @Id
    @Column(name = "scrnnId", length = 30, nullable = false)
    private String scrnnId;

    // 남은 좌석 수
    @Column(name = "avlblSeats")
    private int avlblSeats;

    // 상영일자 (YYYY-MM-DD)
    @Column(name = "scrnnDate")
    @Temporal(TemporalType.DATE)
    private Date scrnnDate;

    // 상영 시작시간 (HH:MM:SS)
    @Column(name = "startScrnn")
    @Temporal(TemporalType.TIME)
    private Time startScrnn;

    // 상영 종료시간 (HH:MM:SS)
    @Column(name = "endScrnn")
    @Temporal(TemporalType.TIME)
    private Time endScrnn;

    // 상영관 아이디
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theaterId")
    private TheaterInfoEntity theaterId;

    // 영화 아이디
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movieId")
    private MovieInfoEntity movieId;

    @Builder
    public ScreenInfoEntity(
            String scrnnId,
            TheaterInfoEntity theaterId,
            MovieInfoEntity movieId,
            int avlblSeats,
            Date scrnnDate,
            Time startScrnn,
            Time endScrnn) {
        this.scrnnId = scrnnId;
        this.theaterId = theaterId;
        this.movieId = movieId;
        this.avlblSeats = avlblSeats;
        this.scrnnDate = scrnnDate;
        this.startScrnn = startScrnn;
        this.endScrnn = endScrnn;
    }
}
