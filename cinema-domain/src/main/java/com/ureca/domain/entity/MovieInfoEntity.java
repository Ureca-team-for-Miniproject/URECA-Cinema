package com.ureca.domain.entity;

import jakarta.persistence.*;
import java.util.Date;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "movieInfo")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovieInfoEntity {
    @Id
    @Column(name = "movieId", length = 20, unique = true, nullable = false)
    // 영화 ID : 장르+개봉일+인덱스3자리 (SF240902001)
    private String movieId;

    @Column(name = "movieNm", length = 100, nullable = false)
    private String movieNm;

    @Column(name = "movieEnNm", length = 100)
    private String movieEnNm;

    @Column(name = "rtngRstrCd", length = 10)
    // 관람등급 AA:전체, A12:12세, A15:15세, A19:19세 이상 관람가
    private String rtngRstrCd;

    @Column(name = "directorNm", length = 100)
    private String directorNm;

    @Column(name = "genreNm", length = 50)
    private String genreNm;

    @Column(name = "genreId", length = 20)
    private String genreId;

    @Column(name = "opnDt", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date opnDt;

    @Column(name = "moviePlot", length = 1000)
    private String moviePlot;

    @Column(name = "cmltAdnc")
    @ColumnDefault("0L")
    // 누적관객수 : Default 0
    private Long cmltAdnc;

    @Column(name = "rank")
    private Integer rank;

    @Column(name = "movieAvlblYn", nullable = false)
    @ColumnDefault("true")
    // 상영여부 true: 상영중(default), false: 상영종료
    private Boolean movieAvlblYn;

    @Column(name = "moviePlayTime", length = 20)
    // 상영시간 : 분 단위
    private String moviePlayTime;

    @Column(name = "movieImgUrl", length = 300)
    private String movieImgUrl;

    @Column(name = "movieVideoUrl", length = 300)
    private String movieVideoUrl;

    // Builder 패턴 : 객체 생성
    @Builder
    public MovieInfoEntity(
            String movieId,
            String movieNm,
            String movieEnNm,
            String rtngRstrCd,
            String directorNm,
            String genreNm,
            String genreId,
            Date opnDt,
            String moviePlot,
            Long cmltAdnc,
            Integer rank,
            Boolean movieAvlblYn,
            String moviePlayTime,
            String movieImgUrl,
            String movieVideoUrl) {
        this.movieId = movieId;
        this.movieNm = movieNm;
        this.movieEnNm = movieEnNm;
        this.rtngRstrCd = rtngRstrCd;
        this.directorNm = directorNm;
        this.genreNm = genreNm;
        this.genreId = genreId;
        this.opnDt = opnDt;
        this.moviePlot = moviePlot;
        this.cmltAdnc = cmltAdnc != null ? cmltAdnc : 0L;
        this.rank = rank;
        this.movieAvlblYn = movieAvlblYn != null ? movieAvlblYn : true;
        this.moviePlayTime = moviePlayTime;
        this.movieImgUrl = movieImgUrl;
        this.movieVideoUrl = movieVideoUrl;
    }
}
