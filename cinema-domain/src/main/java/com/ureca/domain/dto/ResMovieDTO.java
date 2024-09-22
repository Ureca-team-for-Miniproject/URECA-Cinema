package com.ureca.domain.dto;

import com.ureca.domain.entity.MovieInfoEntity;
import lombok.Data;

@Data
public class ResMovieDTO {
    // 영화정보
    private String movieId;
    private String movieNm;
    private String movieEnNm;
    // 개봉일
    private String opnDt;
    // 관람등급
    private String rtngRstrCd;
    // 상영시간
    private String moviePlayTime;
    // 감독
    private String directorNm;
    // 장르
    private String genreNm;
    private String genreId;
    // 영화미디어
    private String movieImgUrl;
    private String movieVideoUrl;
    // 줄거리
    private String moviePlot;
    // 순위
    private Integer rank;
    // 누적관객수
    private Long cmltAdnc;

    // MovieInfoEntity to ResMovieDTO
    public static ResMovieDTO toResMovieDTO(MovieInfoEntity entity) {
        ResMovieDTO dto = new ResMovieDTO();
        dto.setMovieId(entity.getMovieId());
        dto.setMovieNm(entity.getMovieNm());
        dto.setMovieEnNm(entity.getMovieEnNm());
        dto.setOpnDt(entity.getOpnDt() != null ? entity.getOpnDt().toString() : "");
        dto.setRtngRstrCd(entity.getRtngRstrCd());
        dto.setMoviePlayTime(entity.getMoviePlayTime());
        dto.setDirectorNm(entity.getDirectorNm());
        dto.setGenreNm(entity.getGenreNm());
        dto.setGenreId(entity.getGenreId());
        dto.setMovieImgUrl(entity.getMovieImgUrl());
        dto.setMovieVideoUrl(entity.getMovieVideoUrl());
        dto.setMoviePlot(entity.getMoviePlot());
        dto.setRank(entity.getRank());
        dto.setCmltAdnc(entity.getCmltAdnc());
        return dto;
    }
}
