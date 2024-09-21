package com.ureca.domain.repository;

import com.ureca.domain.entity.MovieInfoEntity;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MovieInfoRepository extends JpaRepository<MovieInfoEntity, String> {

    // movieId 검색 : 동일한 장르+개봉일 조합일경우 뒷 3자리 인덱스 증가
    @Query(
            "select max( cast(substring(movieId, length(movieId)-2) as integer) ) from MovieInfoEntity where genreId = :genreId and opnDt = :opnDt")
    String findMaxIdxByGenreAndOpnDt(@Param("genreId") String genreId, @Param("opnDt") Date opnDt);

    // movieNm 검색 : 이미 삽입된 영화는 등록되지 않도록 동일 이름 검색
    List<MovieInfoEntity> findBymovieNm(String movieNm);

    // movieNm Like 검색 : 하나라도 일치할 시 검색
    List<MovieInfoEntity> findByMovieNmContaining(String movieNm);

    // genreNm Like 검색 및 순위로 정렬
    @Query("SELECT m FROM MovieInfoEntity m WHERE m.genreNm LIKE %:genreNm% order by m.rank")
    List<MovieInfoEntity> findByGenreContaining(@Param("genreNm") String genreNm);

    // genreNm Like 검색 및 개봉일로 정렬
    @Query("SELECT m FROM MovieInfoEntity m WHERE m.genreNm LIKE %:genreNm% order by m.opnDt desc")
    List<MovieInfoEntity> findByGenreContainingOpnDtDesc(@Param("genreNm") String genreNm);

    // 정렬 : 누적 예매수 기준 내림차순
    List<MovieInfoEntity> findAllByOrderByCmltAdncDesc();

    // rank update : 누적 예매수 기준 순위 업데이트
    @Modifying
    @Transactional
    @Query("update MovieInfoEntity m SET m.rank = :rank WHERE m.movieId = :movieId")
    void updateMovieRank(@Param("movieId") String movieId, @Param("rank") Integer rank);
}
