package com.ureca.api.controller;

import com.ureca.domain.dto.*;
import com.ureca.domain.service.SeatService;
import com.ureca.domain.service.TicketService;
import com.ureca.domain.service.UserService;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/cinema")
@Controller
public class SeatController {

    private static final Logger logger = LoggerFactory.getLogger(SeatController.class);

    private SeatService seatService;
    private TicketService ticketService;
    private UserService userService;

    public SeatController(
            SeatService seatService, TicketService ticketService, UserService userService) {
        this.seatService = seatService;
        this.ticketService = ticketService;
        this.userService = userService;
    }

    // http://localhost:8080/cinema/seat
    // 상영 스케줄 클릭 시 좌석 선택 화면으로 이동
    @GetMapping("/seat")
    public String seatView(
            HttpSession session, Model model, String scrnnId, String theaterId, String movieId) {
        // 사용자 정보 가져오기
        String userId = "";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) userId = auth.getName();

        ReqSeatDTO reqSeatDTO = new ReqSeatDTO();
        reqSeatDTO.setUserId(userId);
        reqSeatDTO.setMovieId(movieId);
        reqSeatDTO.setTheaterId(theaterId);
        reqSeatDTO.setScrnnId(scrnnId);

        // 서비스 호출 - 좌석 화면 정보 조회하기
        ResSeatDTO resSeatDTO = seatService.getSeatInfo(reqSeatDTO);
        model.addAttribute("ResSeatDTO", resSeatDTO);

        // TODO 화면에서 ResSeatDTO에 맞춰서 화면 구성하도록 수정 (프론트)
        return "movie/seat";
    } // seatView

    // http://localhost:8080/cinema/submitSeats
    // 좌석 선택 완료 버튼 클릭 시 티켓 발행
    @PostMapping("/submitSeats")
    public ResponseEntity<Map<String, Object>> submitSeats(
            HttpSession session, Model model, @RequestBody SeatSaveDTO seatDTO) {

        // TODO (프론트) 개인 적립금 이상은 사용하지 못하도록 설정하는 로직 작성하기
        //      (백엔드) 화면에서 사용한 적립금 DB에서 차감하는 로직 작성하기
        int useAcmltCnt = seatDTO.getUseAcmltCnt(); // 사용적립금
        int pymnAmnt = seatDTO.getPymnAmnt(); // 결제 금액
        logger.info("사용한 적립금:" + useAcmltCnt + " 최종 결제 금액 : " + pymnAmnt);

        // 화면에서 전달받은 좌석 정보 좌석아이디 형태로 매핑
        List<SeatDTO> seatList = seatDTO.getSeatList();
        logger.info("받은 좌석 데이터:");
        seatList.forEach(
                seat ->
                        System.out.println( // ID: E5, Row: E, Col: 5, Type: S
                                "ID: "
                                        + seat.getSeatId()
                                        + ", Row: "
                                        + seat.getSeatRow()
                                        + ", Col: "
                                        + seat.getSeatCol()
                                        + ", Type: "
                                        + seat.getSeatType()));
        String theaterId = seatDTO.getTheaterId(); // 상영관 아이디
        seatList.forEach(
                seat -> {
                    String seatId = theaterId + seat.getSeatId();
                    seat.setSeatId(seatId); // Update seat type
                });

        String userId = ""; // 사용자 아이디
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) userId = auth.getName();

        String scrnnId = seatDTO.getScrnnId(); // 상영 아이디

        String pymnInfo = "카드";

        seatDTO.setUserId(userId);
        seatDTO.setScrnnId(scrnnId);
        seatDTO.setPymnInfo(pymnInfo);

        // 서비스 호출 - 티켓 발행 처리하기
        String savedTicketId = Integer.toString(ticketService.setTicketInfo(seatDTO));
        logger.info("신규 발행 티켓번호 : " + savedTicketId);

        // 성공적으로 발행된 경우, ticketId를 반환
        Map<String, Object> response = new HashMap<>();
        response.put("ticketId", savedTicketId);
        return ResponseEntity.ok(response);
    } // submitSeats
}
