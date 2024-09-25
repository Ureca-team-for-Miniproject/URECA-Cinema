package com.ureca.domain.service;

import com.ureca.common.exception.SeatReservedException;
import com.ureca.domain.dto.*;
import com.ureca.domain.entity.ReservationInfoEntity;
import com.ureca.domain.entity.ScreenInfoEntity;
import com.ureca.domain.entity.SeatInfoEntity;
import com.ureca.domain.entity.TicketInfoEntity;
import com.ureca.domain.repository.ReservationInfoRepository;
import com.ureca.domain.repository.ScreenInfoRepository;
import com.ureca.domain.repository.SeatInfoRepository;
import com.ureca.domain.repository.TicketInfoRepository;
import java.sql.Timestamp;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketService {

    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

    private TicketInfoRepository ticketInfoRepository;
    private ReservationInfoRepository reservationInfoRepository;
    private SeatInfoRepository seatInfoRepository;

    private ScreenInfoRepository screenInfoRepository;

    public TicketService(
            TicketInfoRepository ticketInfoRepository,
            ReservationInfoRepository reservationInfoRepository,
            SeatInfoRepository seatInfoRepository,
            ScreenInfoRepository screenInfoRepository) {
        this.ticketInfoRepository = ticketInfoRepository;
        this.reservationInfoRepository = reservationInfoRepository;
        this.seatInfoRepository = seatInfoRepository;
        this.screenInfoRepository = screenInfoRepository;
    }

    // 티켓 발행 처리
    @Transactional
    public int setTicketInfo(SeatSaveDTO seatDTO) {
        logger.info("seatDTO { " + seatDTO + " }");

        // 1. 예매된 좌석 확인
        for (SeatDTO seatInfo : seatDTO.getSeatList()) {
            String seatId = seatInfo.getSeatId();

            boolean isReserved = reservationInfoRepository.existsBySeatId(seatId);
            if (isReserved) {
                String message = "좌석 " + seatId + " 는 이미 예약되었습니다. 예약을 진행할 수 없습니다.";
                logger.warn(message);
                throw new SeatReservedException(message);
            }
        }

        // 2. 티켓 테이블 업데이트
        seatDTO.setSeatNum(seatDTO.getSeatList().size()); // 인원수

        ScreenInfoEntity scrnnId =
                screenInfoRepository
                        .findById(seatDTO.getScrnnId())
                        .orElseThrow(() -> new RuntimeException("상영 정보를 찾을 수 없습니다."));

        TicketInfoEntity ticketInfo =
                TicketInfoEntity.builder()
                        .scrnnId(scrnnId)
                        .userId(seatDTO.getUserId())
                        .seatNum(seatDTO.getSeatNum())
                        .pymnAmnt(seatDTO.getPymnAmnt())
                        .pymnInfo(seatDTO.getPymnInfo())
                        .pymnDt(new Timestamp(System.currentTimeMillis())) // 현재 시간 설정
                        .build();

        TicketInfoEntity savedTicketInfo = ticketInfoRepository.save(ticketInfo);
        int savedTicketId = savedTicketInfo.getTicketId();
        logger.info("발행된 티켓 아이디 : " + savedTicketId);

        // 3. 예약 테이블 업데이트
        TicketInfoEntity ticket =
                ticketInfoRepository
                        .findById(savedTicketId)
                        .orElseThrow(() -> new RuntimeException("Ticket not found"));

        for (SeatDTO seatInfo : seatDTO.getSeatList()) {
            String seatId = seatInfo.getSeatId();
            SeatInfoEntity seat =
                    seatInfoRepository
                            .findById(seatId)
                            .orElseThrow(() -> new RuntimeException("Seat not found"));

            ReservationInfoEntity reservationInfo =
                    ReservationInfoEntity.builder().ticketId(ticket).seatId(seat).build();

            reservationInfoRepository.save(reservationInfo);
        }

        logger.info("response { success!! }");
        return savedTicketId;
    }

    // 예매 정보 조회
    public ResTicketDTO getTicketInfo(ReqTicketDTO reqTicketDTO) {
        logger.info("reqTicketDTO { " + reqTicketDTO + " }");

        // 1. 단건 조회 - 티켓 정보 조회
        TicketInfoEntity ticket =
                ticketInfoRepository
                        .findById(reqTicketDTO.getTicketId())
                        .orElseThrow(() -> new RuntimeException("Ticket not found"));
        logger.info("ticket { " + ticket + " }");

        ResTicketDTO resTicketDTO =
                ticketInfoRepository.findTicketDetailsByTicketId(ticket.getTicketId());
        logger.info("resTicketDTO { " + resTicketDTO + " }");

        if (resTicketDTO != null) {
            // 2. 목록 조회 - 좌석 정보 조회
            List<SeatDTO> seatTicketList =
                    ticketInfoRepository.findTicketList(resTicketDTO.getTicketId());
            // 좌석 목록을 ResTicketDTO에 설정
            resTicketDTO.setSeatList(seatTicketList);
        }

        return resTicketDTO;
    }
}
