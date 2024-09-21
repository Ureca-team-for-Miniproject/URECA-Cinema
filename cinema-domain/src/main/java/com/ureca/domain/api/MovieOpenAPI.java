package com.ureca.domain.api;

import com.ureca.domain.dto.KmdbDetailMovieDTO;
import com.ureca.domain.dto.KobisDailyMovieDTO;
import java.io.IOException;
import java.util.List;

public interface MovieOpenAPI {
    // 1. KOBIS
    public List<KobisDailyMovieDTO> kobisServiceAPI();

    // 2. KMDB
    public KmdbDetailMovieDTO kmdbServiceAPI(String movieNm) throws IOException;
}
