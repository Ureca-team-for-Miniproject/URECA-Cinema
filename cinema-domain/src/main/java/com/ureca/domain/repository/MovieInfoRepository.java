package com.ureca.domain.repository;


import com.ureca.domain.entity.MovieInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface MovieInfoRepository extends JpaRepository<MovieInfoEntity, String> {

    // movieId 생성 : 동일한 장르+개봉일 조합일경우 뒷 3자리 인덱스 증가
    @Query("select max( cast(substring(movieId, length(movieId)-2) as integer) ) from MovieInfoEntity where genreNm = :genreNm and opnDt = :opnDt")
    String findMaxIdxByGenreAndOpnDt(@Param("genreNm") String genreNm, @Param("opnDt") Date opnDt);
}
