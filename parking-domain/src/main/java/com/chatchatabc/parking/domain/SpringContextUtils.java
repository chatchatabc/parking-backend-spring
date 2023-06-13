package com.chatchatabc.parking.domain;

import com.chatchatabc.parking.domain.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtils implements InitializingBean, BeanFactoryAware {
    private static BeanFactory beanFactory;

    // Repositories
    private static InvoiceRepository invoiceRepository;
    private static ParkingLotRepository parkingLotRepository;
    private static RateRepository rateRepository;
    private static ReportRepository reportRepository;
    private static ReportStatusRepository reportStatusRepository;
    private static RoleRepository roleRepository;
    private static UserRepository userRepository;
    private static VehicleRepository vehicleRepository;

    // Others
    private static ObjectMapper objectMapper;

    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
        SpringContextUtils.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // Repositories
        invoiceRepository = beanFactory.getBean(InvoiceRepository.class);
        parkingLotRepository = beanFactory.getBean(ParkingLotRepository.class);
        rateRepository = beanFactory.getBean(RateRepository.class);
        reportRepository = beanFactory.getBean(ReportRepository.class);
        reportStatusRepository = beanFactory.getBean(ReportStatusRepository.class);
        roleRepository = beanFactory.getBean(RoleRepository.class);
        userRepository = beanFactory.getBean(UserRepository.class);
        vehicleRepository = beanFactory.getBean(VehicleRepository.class);

        // Others
        objectMapper = new ObjectMapper();
    }

    // Repositories
    public static InvoiceRepository getInvoiceRepository() {
        return invoiceRepository;
    }

    public static ParkingLotRepository getParkingLotRepository() {
        return parkingLotRepository;
    }

    public static RateRepository getRateRepository() {
        return rateRepository;
    }

    public static ReportRepository getReportRepository() {
        return reportRepository;
    }

    public static ReportStatusRepository getReportStatusRepository() {
        return reportStatusRepository;
    }

    public static RoleRepository getRoleRepository() {
        return roleRepository;
    }

    public static UserRepository getUserRepository() {
        return userRepository;
    }

    public static VehicleRepository getVehicleRepository() {
        return vehicleRepository;
    }

    // Others
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
