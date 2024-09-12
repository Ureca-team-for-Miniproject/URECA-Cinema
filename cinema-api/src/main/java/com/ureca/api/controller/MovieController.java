package com.ureca.api.controller;

import com.ureca.domain.dto.MovieInfoDTO;
import com.ureca.domain.entity.MovieInfoEntity;
import com.ureca.domain.service.MovieInfoService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

// 영화 URL 공통 접두사 설정
@RequestMapping("/cinema")
@Controller
public class MovieController {

    private MovieInfoService movieService;

    public MovieController(MovieInfoService movieService) {
        this.movieService = movieService;
    }

    // http://localhost:8080/cinema/home
    @GetMapping("/home")
    public String getMovies(
            @RequestParam(name = "movieName", required = false) String movieName, Model model) {

        System.out.println("============================");
        System.out.println("검색어 : " + movieName);

        // 검색어 존재할 경우
        if (movieName != null) {
            List<MovieInfoEntity> movie = movieService.selectMovie(movieName);
            model.addAttribute("movies", movie);
        } else {
            List<MovieInfoEntity> movies = movieService.selectMovies();
            model.addAttribute("movies", movies);
        }

        // TODO : HOM0100 화면 구현
        return "movie/home";
    }

    // (임시) http://localhost:8080/cinema/insert
    @GetMapping("/insert")
    public String insertMovies() {
        // 임의 movieDTO 객체 리스트 생성
        List<MovieInfoDTO> movies = new ArrayList<>();
        MovieInfoDTO dto = new MovieInfoDTO();
        dto.setMovieNm("파일럿");
        dto.setMovieEnNm("Pilot");
        dto.setRtngRstrCd("ALL");
        dto.setDirectorNm("이준호");
        dto.setGenreNm("액션,범죄");
        dto.setGenreId("GACT,GCRI");
        LocalDate opnDate = LocalDate.of(2024, 9, 10);
        Date opnLocalDate = Date.from(opnDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        dto.setOpnDt(opnLocalDate);
        LocalDate endDate = LocalDate.of(2024, 9, 30);
        Date endLocalDate = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        dto.setEndDt(endLocalDate);
        dto.setMoviePlayTime("124");

        MovieInfoDTO dto2 = new MovieInfoDTO();
        dto2.setMovieNm("베테랑2");
        dto2.setMovieEnNm("I, The EXECUTIONER");
        dto2.setRtngRstrCd("A15");
        dto2.setDirectorNm("류승완");
        dto2.setGenreNm("액션,범죄");
        dto2.setGenreId("GACT,GCRI");
        dto2.setOpnDt(opnLocalDate);
        dto2.setEndDt(endLocalDate);
        dto2.setMoviePlayTime("118");

        movies.add(dto);
        movies.add(dto2);

        movieService.insertMovies(movies);

        return "movie/insert";
    }
}
