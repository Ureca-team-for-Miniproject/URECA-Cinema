package com.ureca.domain.dto;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

// 티켓 조회 화면 응답 DTO
@Getter
@Setter
public class ResTicketDTO {

    // 예매 번호
    private int ticketId;
    // 영화 포스터 URL
    private String movieImgUrl;
    // 영화명
    private String movieNm;
    // 관람등급
    private String rtngRstrCd;
    // 상영 시작 시간
    private Time startScrnn;
    // 상영관명
    private String theaterNm;
    // 인원수
    private int seatNum;
    // 취소 가능 여부
    private String cancelYn;
    // 결제일시
    private Timestamp pymnDt;
    // 결제수단
    private String pymnInfo;
    // 결제금액
    private int pymnAmnt;
    // 사용적립금
    private int useAcmltCnt;
    // 예매 좌석 목록
    private List<SeatDTO> seatList = new ArrayList<>(); // 기본값으로 빈 리스트 설정

    public ResTicketDTO(
            int ticketId,
            String movieImgUrl,
            String movieNm,
            String rtngRstrCd,
            Time startScrnn,
            String theaterNm,
            int seatNum,
            String cancelYn,
            Timestamp pymnDt,
            String pymnInfo,
            int pymnAmnt) {
        this.ticketId = ticketId;
        this.movieImgUrl = movieImgUrl;
        this.movieNm = movieNm;
        this.rtngRstrCd = rtngRstrCd;
        this.startScrnn = startScrnn;
        this.theaterNm = theaterNm;
        this.seatNum = seatNum;
        this.cancelYn = cancelYn;
        this.pymnDt = pymnDt;
        this.pymnInfo = pymnInfo;
        this.pymnAmnt = pymnAmnt;
    }
}
