package com.ureca.domain.repository;

import com.ureca.domain.entity.MovieInfoEntity;
import com.ureca.domain.entity.ScreenInfoEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreenInfoRepository extends JpaRepository<ScreenInfoEntity, String> {
    List<ScreenInfoEntity> findByMovieId(MovieInfoEntity movieId);
}
