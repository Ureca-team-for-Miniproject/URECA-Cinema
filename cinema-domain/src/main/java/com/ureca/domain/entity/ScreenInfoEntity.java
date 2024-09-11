package com.ureca.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "screenInfo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScreenInfoEntity {

    @Id
    @Column(name = "scrnnId", length = 30, nullable = false)
    private String scrnnId;

    @Column(name = "avlblSeats")
    private int avlblSeats;

    @Column(name = "scrnnDate")
    @Temporal(TemporalType.DATE)
    private Date scrnnDate;

    @Column(name = "startScrnn")
    @Temporal(TemporalType.TIME)
    private Time startScrnn;

    @Column(name = "endScrnn")
    @Temporal(TemporalType.TIME)
    private Time endScrnn;

    @ManyToOne
    @JoinColumn(name = "theaterId", insertable = false, updatable = false)
    private TheaterInfoEntity theaterId;

    @ManyToOne
    @JoinColumn(name = "movieId", insertable = false, updatable = false)
    private MovieInfoEntity movieId;

    @Builder
    public ScreenInfoEntity(
            String scrnnId
            , TheaterInfoEntity theaterId
            , MovieInfoEntity movieId
            , int avlblSeats
            , Date scrnnDate
            , Time startScrnn
            , Time endScrnn) {
        this.scrnnId = scrnnId;
        this.theaterId = theaterId;
        this.movieId = movieId;
        this.avlblSeats = avlblSeats;
        this.scrnnDate = scrnnDate;
        this.startScrnn = startScrnn;
        this.endScrnn = endScrnn;
    }

}
