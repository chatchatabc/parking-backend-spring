package com.chatchatabc.parking.domain.service;

import com.chatchatabc.parking.TestContainersBaseTest;
import com.chatchatabc.parking.domain.model.Jeepney;
import com.chatchatabc.parking.domain.repository.JeepneyRepository;
import com.chatchatabc.parking.impl.domain.service.JeepneyServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class JeepneyServiceTest extends TestContainersBaseTest {
    @Autowired
    private JeepneyRepository jeepneyRepository;
    @Autowired
    private JeepneyServiceImpl jeepneyService;

    @Test
    void testSaveJeepney_ShouldSaveJeepney() {
        Long count = jeepneyRepository.count();

        Jeepney jeepney = new Jeepney();
        jeepney.setName("Test");
        jeepney.setPlateNumber("BBB-1234");
        jeepney.setLatitude(1.0);
        jeepney.setLongitude(1.0);

        jeepneyService.saveJeepney(jeepney);
        assertThat(jeepneyRepository.count()).isGreaterThan(count);
    }
}