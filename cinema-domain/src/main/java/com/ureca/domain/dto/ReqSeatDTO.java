package com.ureca.domain.dto;

import lombok.Data;

// 좌석 조회 화면 요청 DTO
@Data
public class ReqSeatDTO {

    // 상영 아이디
    private String scrnnId;
    // 상영관 아이디
    private String theaterId;
    // 영화 아이디
    private String movieId;
    // 예매자 아이디
    private String userId;
}
