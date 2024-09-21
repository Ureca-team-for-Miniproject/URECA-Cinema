package com.ureca.domain.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MovieInfoDTO {
    // movieId : genreNm + opnDt + index(auto)
    private String movieId;
    private String movieNm;
    private String movieEnNm;
    private String rtngRstrCd;
    private String directorNm;
    private String genreNm;
    private String genreId;
    private Date opnDt;
    private Date endDt;
    // cmltAdnc : 누적 예매수 count
    // movieRank : cmltAdnc 기준 rank 계산
    // movieAvblYn : opnDt, endDt 기준 상영여부 계산
    private String moviePlayTime;
    // movieImgUrl, movieVideoUrl : AWS S3
}
