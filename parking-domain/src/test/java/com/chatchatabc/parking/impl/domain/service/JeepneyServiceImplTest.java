package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.TestContainersBaseTest;
import com.chatchatabc.parking.domain.model.Jeepney;
import com.chatchatabc.parking.domain.repository.JeepneyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class JeepneyServiceImplTest extends TestContainersBaseTest {
    @Autowired
    private JeepneyServiceImpl jeepneyService;
    @Autowired
    private JeepneyRepository jeepneyRepository;

    @Test
    void testSaveJeepney_ShouldSaveJeepney() {
        Long currentCount = jeepneyRepository.count();

        Jeepney jeepney = new Jeepney();
        jeepney.setPlateNumber("ABC-1234");
        jeepney.setName("Test Jeepney");
        jeepney.setLatitude(1.0);
        jeepney.setLongitude(1.0);
        jeepney.setCapacity(100);
        jeepney.setStatus(Jeepney.JeepneyStatus.DRAFT);
        jeepney.setFlag(0);
        jeepneyService.saveJeepney(jeepney);

        assertThat(jeepneyRepository.count()).isGreaterThan(currentCount);
    }
}