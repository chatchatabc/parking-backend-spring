package com.chatchatabc.parking.domain;

import com.chatchatabc.parking.domain.repository.InvoiceRepository;
import com.chatchatabc.parking.domain.repository.UserRepository;
import com.chatchatabc.parking.domain.repository.VehicleRepository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtils implements InitializingBean, BeanFactoryAware {
    private static BeanFactory beanFactory;
    private static VehicleRepository vehicleRepository;
    private static UserRepository userRepository;
    private static InvoiceRepository invoiceRepository;

    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
        SpringContextUtils.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        vehicleRepository = beanFactory.getBean(VehicleRepository.class);
        userRepository = beanFactory.getBean(UserRepository.class);
        invoiceRepository = beanFactory.getBean(InvoiceRepository.class);
    }

    public static VehicleRepository getVehicleRepository() {
        return vehicleRepository;
    }

    public static UserRepository getUserRepository() {
        return userRepository;
    }

    public static InvoiceRepository getInvoiceRepository() {
        return invoiceRepository;
    }
}
