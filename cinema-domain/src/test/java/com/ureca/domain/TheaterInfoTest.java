package com.ureca.domain;

import com.ureca.domain.entity.TheaterInfoEntity;
import com.ureca.domain.repository.TheaterInfoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TheaterInfoTest {

    @Autowired
    TheaterInfoRepository theaterInfoRepository;

    @Test
    void test() {
        //상영관 테스트 데이터
        TheaterInfoEntity theaterInfo =
                TheaterInfoEntity.builder()
                        .theaterId("103")
                        .theaterNm("3관")
                        .theaterCd("S2")
                        .totalSeats(120)
                        .build();

        //theaterInfo 저장
        theaterInfoRepository.save(theaterInfo);

        System.out.println("===================================");
    }

}
