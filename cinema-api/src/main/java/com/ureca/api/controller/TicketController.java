package com.ureca.api.controller;

import com.ureca.domain.dto.*;
import com.ureca.domain.repository.ScreenInfoRepository;
import com.ureca.domain.service.TicketService;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/cinema")
@Controller
public class TicketController {

    private static final Logger logger = LoggerFactory.getLogger(TicketController.class);

    private TicketService ticketService;
    private ScreenInfoRepository screenInfoRepository;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // http://localhost:8080/cinema/ticket
    // 좌석 선택 완료 후 발행된 티켓 정보 조회
    @GetMapping("/ticket")
    public String ticketView(Model model, @RequestParam int ticketId) {
        logger.info("티켓 정보 조회 !");
        // TODO api 호출 시 ReqTicketDTO 입력받는 방식으로 수정 (L33 ~ 36)
        // 테스트 요청값
        String userId = "gildong94@gmail.com"; // 예매자 아이디
        int useAcmltCnt = 3000; // 사용 적립금
        ReqTicketDTO reqTicketDTO = new ReqTicketDTO();
        reqTicketDTO.setTicketId(ticketId);
        reqTicketDTO.setUserId(userId);
        reqTicketDTO.setUseAcmltCnt(useAcmltCnt);

        // 서비스 호출 - 예매 정보 조회하기
        ResTicketDTO resTicketDTO = ticketService.getTicketInfo(reqTicketDTO);

        if (resTicketDTO != null) {
            if (resTicketDTO.getSeatList() == null) {
                resTicketDTO.setSeatList(new ArrayList<>()); // 빈 리스트로 초기화
            }
            model.addAttribute("ResTicketDTO", resTicketDTO);
        }
        logger.info("ResTicketDTO 이동 !" + resTicketDTO);
        return "movie/ticket";
    }

    // http://localhost:8080/cinema/seatTest
    // 티켓 동시성 테스트용 Controller - curl 로 동시에 호출하는 방식 사용
    @GetMapping("/seatTest")
    public void submitSeatsTest() {

        // 다수의 사용자가 동시에 접근하는 좌석 지정해주기
        SeatDTO seatDTO = new SeatDTO();
        seatDTO.setSeatId("103H5");
        seatDTO.setSeatRow("H");
        seatDTO.setSeatCol(5);
        seatDTO.setSeatType("S");
        seatDTO.setSeatAmount(11000);
        seatDTO.setRsrvYn("N");
        List<SeatDTO> list = new ArrayList<>();
        list.add(seatDTO);

        SeatSaveDTO seatSaveDTO = new SeatSaveDTO();
        seatSaveDTO.setSeatList(list);
        seatSaveDTO.setScrnnId("2D10324091801");
        seatSaveDTO.setUserId("gildong94@gmail.com");
        seatSaveDTO.setSeatNum(1);
        seatSaveDTO.setPymnAmnt(11000);
        seatSaveDTO.setPymnInfo("카드");

        // 서비스 호출 - 티켓 발행 처리하기
        String savedTicketId = Integer.toString(ticketService.setTicketInfo(seatSaveDTO));
        logger.info("신규 발행 티켓번호~!!! : " + savedTicketId);
    } // submitSeatsTest
}
