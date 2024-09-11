package com.ureca.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "movieInfo")
@Getter
@Setter
// @NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(name = "endDt", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date endDt;

    @Column(name = "cmltAdnc")
    // 누적관객수 : Default 0
    private Long cmltAdnc = 0L;

    @Column(name = "movieRank")
    private Integer movieRank;

    @Column(name = "movieAvblYn", nullable = false)
    // 상영여부 true: 상영중(default), false: 상영종료
    private boolean movieAvblYn = true;

    @Column(name = "moviePlayTime", length = 20)
    // 상영시간 : 분 단위
    private String moviePlayTime;

    @Column(name = "movieImgUrl", length = 300)
    private String movieImgUrl;

    @Column(name = "movieVideoUrl", length = 300)
    private String movieVideoUrl;

    /* Builder Pattern 적용 여부 결정 -> Notion에 code 참고 */
}
