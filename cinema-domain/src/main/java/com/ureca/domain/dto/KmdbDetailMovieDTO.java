package com.ureca.domain.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

// KMDB OPEN-API : 영화 상세조회
@Data
@AllArgsConstructor
public class KmdbDetailMovieDTO {
    // 영화명(movieNm), 영문영화명(movieEnNm), 개봉일(opnDt)
    private String title;
    private String titleEng;
    private Date releaseDate;
    //  감독명(directorNm), 장르(genreNm), 줄거리(moviePlot)
    private String directorNm;
    private String genre;
    private String plot;
    // 상영여부(movieAvlblYn), 관람등급(rtngRstrCd), 상영시간(moviePlayTime)
    private Boolean movieAvlblYn;
    private String rating;
    private String runTime;
    // 포스터 URL(movieImgUrl), 예고편 URL(movieVideoUrl)
    private String posterUrl;
    private String videoUrl;
}
