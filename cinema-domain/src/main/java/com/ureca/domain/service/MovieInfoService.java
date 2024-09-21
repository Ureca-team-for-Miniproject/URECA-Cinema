package com.ureca.domain.service;

import com.ureca.domain.api.MovieOpenAPI;
import com.ureca.domain.dto.KmdbDetailMovieDTO;
import com.ureca.domain.dto.KobisDailyMovieDTO;
import com.ureca.domain.dto.ResMovieDTO;
import com.ureca.domain.dto.ResTimeTblInfoDTO;
import com.ureca.domain.entity.MovieInfoEntity;
import com.ureca.domain.entity.ScreenInfoEntity;
import com.ureca.domain.repository.MovieInfoRepository;
import com.ureca.domain.repository.ScreenInfoRepository;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovieInfoService {
    public static final int START_ID_INDEX = 1;

    private MovieInfoRepository movieRepository;
    private ScreenInfoRepository screenInfoRepository;
    private MovieOpenAPI movieOpenApi;

    public MovieInfoService(
            MovieInfoRepository movieRepository,
            MovieOpenAPI movieOpenApi,
            ScreenInfoRepository screenInfoRepository) {
        this.movieRepository = movieRepository;
        this.movieOpenApi = movieOpenApi;
        this.screenInfoRepository = screenInfoRepository;
    }

    // TODO : CommonCode Table로 대체
    private static final Map<String, String> genreMap = new HashMap<>();
    private static final Map<String, String> ageMap = new HashMap<>();

    static {
        genreMap.put("다큐멘터리", "GDAC");
        genreMap.put("애니메이션", "GANI");
        genreMap.put("코메디", "GCOM");
        genreMap.put("판타지", "GFAN");
        genreMap.put("액션", "GACT");
        genreMap.put("어드벤처", "GADV");
        genreMap.put("드라마", "GDRA");
        genreMap.put("SF", "GSF");
        genreMap.put("로맨스", "GROM");
        genreMap.put("멜로", "GMEL");
        genreMap.put("범죄", "GCRI");
        genreMap.put("가족", "GFAM");
        genreMap.put("호러", "GHOR");
        genreMap.put("미스터리", "GMYS");
        genreMap.put("스릴러", "GTHR");
        genreMap.put("뮤지컬", "GMUS");
        genreMap.put("뮤직", "GMSC");
        genreMap.put("자연", "GNAT");
        genreMap.put("인물", "GPEO");
        ageMap.put("전체관람가", "AA");
        ageMap.put("12세이상관람가", "A12");
        ageMap.put("15세이상관람가", "A15");
        ageMap.put("청소년관람불가", "A19");
    }

    // 1. INSERT : KOBIS,KMDB DTO로 MovieInfoEntity 생성
    @Transactional
    public List<ResMovieDTO> KobisKmdbDTOtoEntity() throws IOException {
        List<MovieInfoEntity> entityList = new ArrayList<>();
        List<ResMovieDTO> dtoList = new ArrayList<>();

        // * KOBIS : 일별 영화 박스오피스 가져오기
        List<KobisDailyMovieDTO> kobisDtoList = movieOpenApi.kobisServiceAPI();
        for (KobisDailyMovieDTO kobisDailyMovieDTO : kobisDtoList) {
            String movieNm = kobisDailyMovieDTO.getMovieNm();

            // * KMDB : 각 영화 상세 정보 가져오기
            KmdbDetailMovieDTO kmdbDto = movieOpenApi.kmdbServiceAPI(movieNm);
            if (kmdbDto == null) continue;
            String genreId = getGenreIds(kmdbDto.getGenre());
            String movieId = generateMovieId(genreId, kmdbDto.getReleaseDate());
            String age = getAge(kmdbDto.getRating());

            // Entity 생성
            MovieInfoEntity entity =
                    MovieInfoEntity.builder()
                            .movieId(movieId)
                            .movieNm((kmdbDto.getTitle()))
                            .movieEnNm(kmdbDto.getTitleEng())
                            .rtngRstrCd(age)
                            .directorNm(kmdbDto.getDirectorNm())
                            .genreNm(kmdbDto.getGenre())
                            .genreId(genreId)
                            .opnDt(kmdbDto.getReleaseDate())
                            .moviePlot(kmdbDto.getPlot())
                            .rank(kobisDailyMovieDTO.getRank())
                            .movieAvlblYn(kmdbDto.getMovieAvlblYn())
                            .moviePlayTime(kmdbDto.getRunTime())
                            .movieImgUrl(kmdbDto.getPosterUrl())
                            .movieVideoUrl(kmdbDto.getVideoUrl())
                            .build();

            // 동일한 영화명 조회
            List<MovieInfoEntity> existingMovies = movieRepository.findBymovieNm(movieNm);
            // 중복 X : DB 반영
            if (!isDuplicate(entity, existingMovies)) {
                entityList.add(entity);
                movieRepository.save(entity);
                movieRepository.flush();
                System.out.println("등록된 영화: " + entity.toString());
                ResMovieDTO dto = ResMovieDTO.toResMovieDTO(entity);
                dtoList.add(dto);
            }
        }
        return dtoList;
    }

    // 1. INSERT 기초작업 : 장르 아이디 생성
    public static String getGenreIds(String genre) {
        // 쉼표 기준 장르 분리
        String[] genres = genre.split(",");
        StringBuilder genreIds = new StringBuilder();
        // 각 장르별 포함될 경우 : 장르 ID에 "," 구분자로 추가
        for (String g : genres) {
            g = g.trim();
            if (genreIds.length() < 10 && genreMap.containsKey(g)) {
                if (genreIds.length() > 0) {
                    genreIds.append(",");
                }
                genreIds.append(genreMap.getOrDefault(g, "GNON"));
            }
        }
        return genreIds.toString();
    }

    // 1. INSERT 기초작업 : 영화 아이디 생성 : 동시실행
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String generateMovieId(String genreId, Date opnDt) {
        return makeMovieId(genreId, opnDt);
    }

    // 1. INSERT 기초작업 : 중복 장르별 고유번호(끝 숫자 3자리) 최댓값 검색해 생성
    @Transactional(propagation = Propagation.REQUIRES_NEW)
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
        String newMovieId = firstGenreId + formattedDate + newIndexStr;
        return newMovieId;
    }

    // 1. INSERT 기초작업 : 영화 관람등급 생성
    public static String getAge(String age) {
        StringBuilder ageSb = new StringBuilder();
        if (ageMap.containsKey(age)) {
            ageSb.append(ageMap.getOrDefault(age, "AA"));
        }
        return ageSb.toString();
    }

    // 1. INSERT : 중복 영화 존재 확인
    private boolean isDuplicate(MovieInfoEntity entity, List<MovieInfoEntity> existingMovies) {
        return existingMovies.stream()
                .anyMatch(
                        existingMovie ->
                                existingMovie.getMovieNm().equals(entity.getMovieNm())
                                        && existingMovie
                                                .getDirectorNm()
                                                .equals(entity.getDirectorNm())
                                        && existingMovie.getOpnDt().equals(entity.getOpnDt())
                                        && existingMovie
                                                .getMoviePlot()
                                                .equals(entity.getMoviePlot()));
    }

    // 2. SELECT : Entity List to DTO List 함수
    private List<ResMovieDTO> movieToDtoList(List<MovieInfoEntity> entityList) {
        List<ResMovieDTO> dtoList = new ArrayList<>();
        for (MovieInfoEntity entity : entityList) {
            ResMovieDTO dto = ResMovieDTO.toResMovieDTO(entity);
            dtoList.add(dto);
        }
        return dtoList;
    }

    // 2. SELECT : 영화 리스트 전체 조회
    public List<ResMovieDTO> selectMovies() {
        List<MovieInfoEntity> entityList = movieRepository.findAllByOrderByCmltAdncDesc();
        return movieToDtoList(entityList);
    }

    // 2. SELECT : 영화 이름으로 일부 조회 (Like)
    public List<ResMovieDTO> selectMovieNm(String movieName) {
        List<MovieInfoEntity> entityList = movieRepository.findByMovieNmContaining(movieName);
        return movieToDtoList(entityList);
    }

    // 2. SELECT : 영화 아이디로 일부 조회
    public ResMovieDTO selectMovieId(String movieId) {
        Optional<MovieInfoEntity> movieEntity = movieRepository.findById(movieId);
        if (movieEntity.isPresent()) {
            return ResMovieDTO.toResMovieDTO(movieEntity.get());
        } else {
            throw new EntityNotFoundException("영화 ID에 해당하는 영화를 찾을 수 없습니다: " + movieId);
        }
    }

    // 2. SELECT : 영화 아이디로 상영 시간 리스트 조회
    public List<ResTimeTblInfoDTO> selectScreenId(ResMovieDTO movieDTO) {
        MovieInfoEntity movieEntity = movieRepository.findById(movieDTO.getMovieId()).get();
        List<ScreenInfoEntity> screenEntityList = screenInfoRepository.findByMovieId(movieEntity);
        List<ResTimeTblInfoDTO> screenDtoList = new ArrayList<>();
        for (ScreenInfoEntity screenInfoEntity : screenEntityList) {
            screenDtoList.add(ResTimeTblInfoDTO.toResTimeTblInfoDTO(screenInfoEntity));
        }
        return screenDtoList;
    }

    // 2. SELECT : 장르로 일부 조회 (Like)
    public List<ResMovieDTO> selectMovieGenreNm(String genreNm) {
        List<MovieInfoEntity> entityList = movieRepository.findByGenreContaining(genreNm);
        return movieToDtoList(entityList);
    }

    // 3. UPDATE : 영화 순위 업데이트
    public void updateMovieRank() {
        List<MovieInfoEntity> entityList = movieRepository.findAllByOrderByCmltAdncDesc();
        for (int i = 0; i < entityList.size(); i++) {
            MovieInfoEntity entity = entityList.get(i);
            movieRepository.updateMovieRank(entity.getMovieId(), i + 1);
        }
    }

    // 4. 단일 INSERT : KMDB로 영화 상세 가져오기
    @Transactional
    public KmdbDetailMovieDTO kmdbGetDTO(String movieNm) throws IOException {
        System.out.println("\n영화명: " + movieNm);
        KmdbDetailMovieDTO kmdbDto = movieOpenApi.kmdbServiceAPI(movieNm);
        if (kmdbDto == null) return null;
        System.out.println("dto: " + kmdbDto.toString());
        String genreId = getGenreIds(kmdbDto.getGenre());
        String movieId = generateMovieId(genreId, kmdbDto.getReleaseDate());
        String age = getAge(kmdbDto.getRating());
        // Entity 생성
        MovieInfoEntity entity =
                MovieInfoEntity.builder()
                        .movieId(movieId)
                        .movieNm((kmdbDto.getTitle()))
                        .movieEnNm(kmdbDto.getTitleEng())
                        .rtngRstrCd(age)
                        .directorNm(kmdbDto.getDirectorNm())
                        .genreNm(kmdbDto.getGenre())
                        .genreId(genreId)
                        .opnDt(kmdbDto.getReleaseDate())
                        .moviePlot(kmdbDto.getPlot())
                        .rank(100)
                        .movieAvlblYn(kmdbDto.getMovieAvlblYn())
                        .moviePlayTime(kmdbDto.getRunTime())
                        .movieImgUrl(kmdbDto.getPosterUrl())
                        .movieVideoUrl(kmdbDto.getVideoUrl())
                        .build();
        // 동일한 영화명 조회
        List<MovieInfoEntity> existingMovies = movieRepository.findBymovieNm(movieNm);
        boolean isDuplicate =
                existingMovies.stream()
                        .anyMatch(
                                existingMovie ->
                                        existingMovie.getMovieNm().equals(entity.getMovieNm())
                                                && existingMovie
                                                        .getDirectorNm()
                                                        .equals(entity.getDirectorNm())
                                                && existingMovie
                                                        .getOpnDt()
                                                        .equals(entity.getOpnDt())
                                                && existingMovie
                                                        .getMoviePlot()
                                                        .equals(entity.getMoviePlot()));
        // 기존에 없으면
        if (!isDuplicate) {
            movieRepository.save(entity);
            movieRepository.flush();
        }
        return kmdbDto;
    }
}
