package com.ureca.domain.service;

import com.ureca.domain.entity.MemberEntity;
import com.ureca.domain.entity.MovieInfoEntity;
import com.ureca.domain.repository.MemberRepository;
import com.ureca.domain.repository.MovieInfoRepository;
import com.ureca.domain.repository.TicketInfoRepository;
import java.time.LocalDate;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class MovieRecommendService {

    private MemberRepository memberRepository;
    private TicketInfoRepository ticketInfoRepository;
    private MovieInfoRepository movieInfoRepository;

    public MovieRecommendService(
            MemberRepository memberRepository,
            TicketInfoRepository ticketInfoRepository,
            MovieInfoRepository movieInfoRepository) {
        this.memberRepository = memberRepository;
        this.ticketInfoRepository = ticketInfoRepository;
        this.movieInfoRepository = movieInfoRepository;
    }

    // 최종 추천로직 : <사용자, 인기순(4개)+장르순(<=4개) 영화목록> return
    public Map<MemberEntity, List<MovieInfoEntity>> recommendAlgo() {
        Map<MemberEntity, List<MovieInfoEntity>> memberMovieMap = new HashMap<>();
        // 1. 휴면회원
        List<MemberEntity> sleeperMember = getSleeperMembers();
        // 회원별
        for (MemberEntity member : sleeperMember) {
            // 4. 인기영화 추가
            List<MovieInfoEntity> movieList = new ArrayList<>();
            movieList.addAll(recommendRankMovie());
            // 2. 회원별 예매내역 (장르 count) 확인
            Map<String, Integer> genreCountsMap = getGenreCounts(member.getId());
            if (genreCountsMap != null) {
                // 3. 장르기반영화 추가
                movieList.addAll(recommendGenreMovie(genreCountsMap));
            }
            memberMovieMap.put(member, movieList);
        }
        return memberMovieMap;
    }

    // 1. 휴면회원 찾기
    public List<MemberEntity> getSleeperMembers() {
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        return memberRepository.findSleeperMembers(oneYearAgo);
    }

    // 2. 사용자별 예매내역 확인 : 미존재시 null, 존재시 Map (장르, count) return
    public Map<String, Integer> getGenreCounts(String userId) {
        // 회원별 장르
        List<Object[]> genreStrCountObj = ticketInfoRepository.findGenresByUserId(userId);
        // 예매내역 X
        if (genreStrCountObj.isEmpty()) {
            return null;
        }
        // 예매내역 O
        Map<String, Integer> genreCount = new HashMap<>();
        for (Object[] result : genreStrCountObj) {
            String genre = (String) result[0];
            Integer count = ((Long) result[1]).intValue();
            // 장르 ',' 구분자로 연결되어있을 경우 처리
            if (genre.contains(",")) {
                for (String g : genre.split(",")) {
                    g = g.trim();
                    if (!g.isEmpty()) {
                        genreCount.put(g, genreCount.getOrDefault(g, 0) + count);
                    }
                }
            } else {
                genreCount.put(genre, genreCount.getOrDefault(genre, 0) + count);
            }
        }
        // 값 기준 내림차순 정렬
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(genreCount.entrySet());
        Collections.sort(entryList, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        Map<String, Integer> genreCountSort = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : entryList) {
            genreCountSort.put(entry.getKey(), entry.getValue());
        }
        return genreCountSort;
    }

    // 3. 장르 기반 영화 추천
    public List<MovieInfoEntity> recommendGenreMovie(Map<String, Integer> genreCountSort) {
        System.out.println("장르 기반 Map: " + genreCountSort.toString());
        List<MovieInfoEntity> movieList = new ArrayList<>();
        // 최다 장르별 영화추천
        for (String genre : genreCountSort.keySet()) {
            System.out.println("장르: " + genre);
            List<MovieInfoEntity> movies =
                    movieInfoRepository.findByGenreContainingOpnDtDesc(genre);
            for (MovieInfoEntity movieInfoEntity : movies) {
                if (movieList.size() >= 4) return movieList;
                movieList.add(movieInfoEntity);
            }
        }
        return movieList;
    }

    // 4. 인기순 기반 영화추천
    public List<MovieInfoEntity> recommendRankMovie() {
        List<MovieInfoEntity> movieList = new ArrayList<>();
        // 순위별 영화추천
        List<MovieInfoEntity> movieAllList = movieInfoRepository.findAllByOrderByCmltAdncDesc();
        for (int i = 0; i < 4; i++) movieList.add(movieAllList.get(i));
        return movieList;
    }
}
