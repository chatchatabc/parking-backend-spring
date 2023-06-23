package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.domain.model.Route;
import com.chatchatabc.parking.domain.repository.RouteRepository;
import com.chatchatabc.parking.domain.service.RouteService;
import org.springframework.stereotype.Service;

@Service
public class RouteServiceImpl implements RouteService {
    private final RouteRepository routeRepository;

    public RouteServiceImpl(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    /**
     * Save route
     *
     * @param route route
     */
    @Override
    public Route saveRoute(Route route) {
        return routeRepository.save(route);
    }
}
