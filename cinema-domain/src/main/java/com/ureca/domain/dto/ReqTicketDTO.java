package com.ureca.domain.dto;

import lombok.Getter;
import lombok.Setter;

// 티켓 조회 화면 요청 DTO
@Getter
@Setter
public class ReqTicketDTO {

    // 예매 번호
    private int ticketId;
    // 예매자 아이디
    private String userId;
    // 사용 적립금
    private int useAcmltCnt;
}
