package com.ureca.domain.service;

import com.ureca.domain.dto.MovieInfoDTO;
import com.ureca.domain.entity.MovieInfoEntity;
import com.ureca.domain.repository.MovieInfoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieInfoService {
    public static final int START_ID_INDEX = 1;

    private MovieInfoRepository movieRepository;

    public MovieInfoService(MovieInfoRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // 영화 아이디 생성 (한 번에 동시 실행)
    @Transactional
    public String makeMovieId(String genreId, Date opnDt) {
        // @Temporal Date -> LocalDate -> String format
        LocalDate localDate = opnDt.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        String formattedDate = localDate.format(DateTimeFormatter.ofPattern("yyMMdd"));
        // 동일한 장르와 개봉일 존재: 자동증가 숫자 3자리 확인
        String maxIndexStr = movieRepository.findMaxIdxByGenreAndOpnDt(genreId, opnDt);
        // 존재 안할 시: 1부터 증가
        int newIndex = (maxIndexStr == null) ? START_ID_INDEX : Integer.parseInt(maxIndexStr) + 1;
        String newIndexStr = String.format("%03d", newIndex);
        System.out.println("새로운 인덱스 숫자 3자리: " + newIndexStr);
        // 장르+개봉일+자동증가 숫자 3자리
        return genreId + formattedDate + newIndexStr;
    }

    // 영화 추가 (DTO-Entity 변환)
    @Transactional
    public void insertMovies(List<MovieInfoDTO> movies) {
        List<MovieInfoEntity> entities = movies.stream().map(dto -> {
            MovieInfoEntity entity = new MovieInfoEntity();
            entity.setMovieId(makeMovieId(dto.getGenreId(), dto.getOpnDt()));
            entity.setMovieNm(dto.getMovieNm());
            entity.setMovieEnNm(dto.getMovieEnNm());
            entity.setRtngRstrCd(dto.getRtngRstrCd());
            entity.setDirectorNm(dto.getDirectorNm());
            entity.setGenreNm(dto.getGenreNm());
            entity.setGenreId(dto.getGenreId());
            entity.setOpnDt(dto.getOpnDt());
            entity.setEndDt(dto.getEndDt());
            entity.setMoviePlayTime(dto.getMoviePlayTime());
            // System.out.println("생성된 entity 객체 ID: " + entity.getMovieId());
            return entity;
        }).collect(Collectors.toList());

        movieRepository.saveAll(entities);
        // DB 반영
        movieRepository.flush();
    }
}
