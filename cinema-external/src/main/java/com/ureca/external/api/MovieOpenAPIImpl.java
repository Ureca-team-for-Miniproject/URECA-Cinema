package com.ureca.external.api;

import com.ureca.domain.api.MovieOpenAPI;
import com.ureca.domain.dto.KmdbDetailMovieDTO;
import com.ureca.domain.dto.KobisDailyMovieDTO;
import com.ureca.external.service.KmdbOpenAPIRestService;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import kr.or.kobis.kobisopenapi.consumer.rest.KobisOpenAPIRestService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MovieOpenAPIImpl implements MovieOpenAPI {

    // 1. 인증키, URL
    String kobisKey = "";
    String kmdbKey = "";
    String kmdbHost =
            "https://api.koreafilm.or.kr/openapi-data2/wisenut/search_api/search_json2.jsp";

    // 2. KOBIS OPEN-API : return List<KobisDailyMovieDTO>
    public List<KobisDailyMovieDTO> kobisServiceAPI() {
        // 결과 DTO List
        List<KobisDailyMovieDTO> dtoList = new ArrayList<>();
        // 필수인자
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        String targetDt = yesterday.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        try {
            // KOBIS Request, Response
            KobisOpenAPIRestService service = new KobisOpenAPIRestService(kobisKey);
            String dailyResponse = service.getDailyBoxOffice(true, targetDt, "", "", "", "");
            // JSON 문자열 -> Object 객체로 파싱
            JSONParser jsonParser = new JSONParser();
            Object object = jsonParser.parse(dailyResponse);
            JSONObject jsonObject = (JSONObject) object;
            // JSON 구조: <boxOfiiceResult> <dailyBoxOfficeList> <dailBoxOffice> ... </dailBoxOffice>
            JSONObject parseBoxOfiiceResult = (JSONObject) jsonObject.get("boxOfficeResult");
            JSONArray parseDailyBoxOfficeList =
                    (JSONArray) parseBoxOfiiceResult.get("dailyBoxOfficeList");
            // 영화코드, 영화명, 순위, 개봉일 추출
            for (Object o : parseDailyBoxOfficeList) {
                JSONObject dailyBoxOffice = (JSONObject) o;
                // DTO 생성
                dtoList.add(
                        new KobisDailyMovieDTO(
                                (String) dailyBoxOffice.get("movieCd"),
                                (String) dailyBoxOffice.get("movieNm"),
                                Integer.parseInt((String) dailyBoxOffice.get("rank")),
                                new SimpleDateFormat("yyyy-MM-dd")
                                        .parse((String) dailyBoxOffice.get("openDt"))));
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return dtoList;
    }

    // 3. KMDB OPEN-API : return KmdbDetailMovieDTO
    public KmdbDetailMovieDTO kmdbServiceAPI(String movieNm) throws IOException {
        // 결과 DTO
        KmdbDetailMovieDTO dto = null;
        try {
            // KMDB Request, Response
            KmdbOpenAPIRestService service = new KmdbOpenAPIRestService(kmdbKey, kmdbHost);
            String detailResponse = service.getDetailMovie(movieNm);
            // JSON 문자열 -> Object 객체로 파싱
            JSONParser jsonParser = new JSONParser();
            Object object = jsonParser.parse(detailResponse);
            JSONObject jsonObject = (JSONObject) object;
            // JSON 구조 : <Data> <Result> <title></title> ...
            JSONObject parseData = (JSONObject) ((JSONArray) jsonObject.get("Data")).get(0);
            JSONObject parseResult = (JSONObject) ((JSONArray) parseData.get("Result")).get(0);
            // DTO 생성 함수
            dto = buildKmdbDetailMovieDTO(parseResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    // DTO 생성 함수
    private KmdbDetailMovieDTO buildKmdbDetailMovieDTO(JSONObject parseResult)
            throws ParseException {
        // 영화명, 영화명영문, 관람등급
        String title = cleanString((String) parseResult.get("title"));
        String titleEng = cleanString((String) parseResult.get("titleEng"));
        String rating = (String) parseResult.get("rating");
        // 감독명 : "directors":{ "director:[ {"directorNm": }, ...] }
        JSONObject directors = (JSONObject) parseResult.get("directors");
        JSONObject director = (JSONObject) ((JSONArray) directors.get("director")).get(0);
        String directorNm = (String) director.get("directorNm");
        // 장르, 개봉일 : Date Formatting
        String genre = (String) parseResult.get("genre");
        String repRlsDate = (String) parseResult.get("repRlsDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date releaseDate = sdf.parse(repRlsDate);
        // 줄거리 : "plots":{ "plot": [ {"plotText": }, ...] }
        JSONObject plots = (JSONObject) parseResult.get("plots");
        JSONObject plot = (JSONObject) ((JSONArray) plots.get("plot")).get(0);
        String plotText = (String) plot.get("plotText");
        // 상영여부 : 개봉일<오늘 계산
        Date today = new Date();
        Boolean movieAvlblYn = today.after(releaseDate);
        // 상영시간, 포스터, 영상
        String runTime = (String) parseResult.get("runtime");
        String posters = (String) parseResult.get("posters");
        String posterURL = posters.split("\\|")[0];
        String stlls = (String) parseResult.get("stlls");
        String videoURL = stlls.split("\\|")[0];
        return new KmdbDetailMovieDTO(
                title,
                titleEng,
                releaseDate,
                directorNm,
                genre,
                plotText,
                movieAvlblYn,
                rating,
                runTime,
                posterURL,
                videoURL);
    }

    private String cleanString(String input) {
        return input.replace("!HS", "").replace("!HE", "").replaceAll("\\s+", " ").trim();
    }
}
