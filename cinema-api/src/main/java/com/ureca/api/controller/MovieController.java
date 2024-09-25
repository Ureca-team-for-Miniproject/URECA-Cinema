package com.ureca.api.controller;

import com.ureca.domain.dto.KmdbDetailMovieDTO;
import com.ureca.domain.dto.ResMovieDTO;
import com.ureca.domain.dto.ResTimeTblInfoDTO;
import com.ureca.domain.service.MovieInfoService;
import com.ureca.domain.service.MovieRecommendService;
import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// 영화 URL 공통 접두사 설정
@RequestMapping("/cinema")
@Controller
public class MovieController {

    private MovieInfoService movieService;
    private MovieRecommendService movieRecommendService;

    public MovieController(
            MovieInfoService movieService, MovieRecommendService movieRecommendService) {
        this.movieService = movieService;
        this.movieRecommendService = movieRecommendService;
    }

    // (1) HOM0100 : http://localhost:8080/cinema/home
    @GetMapping("/home")
    public String home(
            @RequestParam(name = "searchWord", required = false) String searchWord,
            @RequestParam(name = "genreName", required = false) String genreName,
            Model model) {
        // 검색어 X
        if (searchWord == null || searchWord.isEmpty() || searchWord.equals("")) {
            // 선택 장르 X
            if (genreName == null || genreName.isEmpty() || genreName.equals("")) {
                List<ResMovieDTO> movies = movieService.selectMovies();
                model.addAttribute("MovieList", movies);
                // 선택 장르 O
            } else {
                List<ResMovieDTO> movies = movieService.selectMovieGenreNm(genreName);
                for (ResMovieDTO movieDTO : movies) {
                    System.out.println(movieDTO.toString());
                }
                model.addAttribute("MovieList", movies);
            }
            // 검색어 O
        } else {
            List<ResMovieDTO> movie = movieService.selectMovieNm(searchWord);
            for (ResMovieDTO movieDTO : movie) {
                System.out.println(movieDTO.toString());
            }
            model.addAttribute("MovieList", movie);
        }
        return "movie/home";
    }

    // (2) MOV0100 : http://localhost:8080/cinema/movieInfo
    @GetMapping("/movieInfo")
    public String movieInfo(@RequestParam String movieId, Model model) {
        // 영화
        ResMovieDTO movieDTO = movieService.selectMovieId(movieId);
        System.out.println("선택한 영화: " + movieDTO.toString());
        model.addAttribute("MovieInfo", movieDTO);
        // 상영정보
        List<ResTimeTblInfoDTO> timeDTOList = movieService.selectScreenId(movieDTO);
        System.out.println("해당 상영시간: " + timeDTOList.toString());
        model.addAttribute("TimeTblInfo", timeDTOList);
        return "movie/movieInfo";
    }

    // all insert : http://localhost:8080/cinema/insert
    @ResponseBody
    @GetMapping("/insert")
    public ResponseEntity<List<ResMovieDTO>> insertMovies() {
        try {
            List<ResMovieDTO> movies = movieService.KobisKmdbDTOtoEntity();
            return ResponseEntity.ok(movies);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // one insert : http://localhost:8080/cinema/kmdb?movieName=
    @ResponseBody
    @GetMapping("/kmdb")
    public ResponseEntity<KmdbDetailMovieDTO> kmdb(
            @RequestParam(name = "movieName", required = false) String movieName)
            throws IOException {
        try {
            KmdbDetailMovieDTO kmdbDto = movieService.kmdbGetDTO(movieName);
            return ResponseEntity.ok(kmdbDto);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // update : http://localhost:8080/cinema/update
    @PostMapping("/update")
    public ResponseEntity<String> update() {
        movieService.updateMovieRank();
        return ResponseEntity.ok("누적 예매수 기준 Rank Update 성공");
    }

    // recommend
    //    @ResponseBody
    //    @GetMapping("/recommend")
    //    public Map<MemberEntity, List<MovieInfoEntity>> recommend() {
    //        return movieRecommendService.recommendAlgo();
    //    }
}
