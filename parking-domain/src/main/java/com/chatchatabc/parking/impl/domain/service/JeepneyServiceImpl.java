package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.domain.model.Jeepney;
import com.chatchatabc.parking.domain.repository.JeepneyRepository;
import com.chatchatabc.parking.domain.service.JeepneyService;
import org.springframework.stereotype.Service;

@Service
public class JeepneyServiceImpl implements JeepneyService {
    private final JeepneyRepository jeepneyRepository;

    public JeepneyServiceImpl(JeepneyRepository jeepneyRepository) {
        this.jeepneyRepository = jeepneyRepository;
    }

    /**
     * Save jeepney
     *
     * @param jeepney jeepney to save
     */
    @Override
    public void saveJeepney(Jeepney jeepney) {
        jeepneyRepository.save(jeepney);
    }
}
