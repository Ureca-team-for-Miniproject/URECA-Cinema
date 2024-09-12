package com.ureca.domain.service;

import com.ureca.domain.dto.MovieInfoDTO;
import com.ureca.domain.entity.MovieInfoEntity;
import com.ureca.domain.repository.MovieInfoRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MovieInfoService {
    public static final int START_ID_INDEX = 1;

    private MovieInfoRepository movieRepository;

    public MovieInfoService(MovieInfoRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // 1. SELECT : 영화 리스트 전체 조회
    public List<MovieInfoEntity> selectMovies() {
        return movieRepository.findAll();
    }

    // 영화 일부 조회
    public List<MovieInfoEntity> selectMovie(String movieName) {
        return movieRepository.findBymovieNm(movieName);
    }

    // 2. INSERT : 영화 아이디 생성 위한 중복 장르별 고유번호(끝 숫자 3자리) 최댓값 검색 (동시 실행)
    @Transactional
    public String makeMovieId(String genreId, Date opnDt) {
        // @Temporal Date -> LocalDate -> String format
        LocalDate localDate = opnDt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String formattedDate = localDate.format(DateTimeFormatter.ofPattern("yyMMdd"));
        // 동일한 장르와 개봉일 존재: 자동증가 숫자 3자리 확인
        String maxIndexStr = movieRepository.findMaxIdxByGenreAndOpnDt(genreId, opnDt);
        // 존재 안할 시: 1부터 증가
        int newIndex = (maxIndexStr == null) ? START_ID_INDEX : Integer.parseInt(maxIndexStr) + 1;
        String newIndexStr = String.format("%03d", newIndex);
        // 사전 순 첫 장르
        String[] genreIds = genreId.split(",");
        Arrays.sort(genreIds);
        String firstGenreId = genreIds[0];
        // 장르+개봉일+자동증가 숫자 3자리
        return firstGenreId + formattedDate + newIndexStr;
    }

    // 2. INSERT : 영화 추가
    @Transactional
    public void insertMovies(List<MovieInfoDTO> movieInfoDTOList) {

        for (MovieInfoDTO dto : movieInfoDTOList) {
            // 새로운 영화 아이디 생성
            String newMovieId = makeMovieId(dto.getGenreId(), dto.getOpnDt());
            dto.setMovieId(newMovieId);
            // DTO -> Entity
            MovieInfoEntity movieInfoEntity = MovieInfoEntity.toEntity(dto);
            // DB 반영
            movieRepository.save(movieInfoEntity);
            movieRepository.flush();
        }
    }
}
