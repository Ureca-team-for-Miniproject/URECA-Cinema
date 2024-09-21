package com.ureca.domain.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

// Kobis OPEN-API : 일별 박스오피스 조회
@Data
@AllArgsConstructor
public class KobisDailyMovieDTO {
    private String movieCd;
    private String movieNm;
    private Integer rank;
    private Date openDt;
}
