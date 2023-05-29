package com.chatchatabc.parking.impl.domain.service.log;

import com.chatchatabc.parking.domain.model.log.UserLogoutLog;
import com.chatchatabc.parking.domain.repository.log.UserLogoutLogRepository;
import com.chatchatabc.parking.domain.service.log.UserLogoutLogService;
import org.springframework.stereotype.Service;

@Service
public class UserLogoutLogServiceImpl implements UserLogoutLogService {
    private final UserLogoutLogRepository userLogoutLogRepository;

    public UserLogoutLogServiceImpl(UserLogoutLogRepository userLogoutLogRepository) {
        this.userLogoutLogRepository = userLogoutLogRepository;
    }

    /**
     * Create user logout log
     *
     * @param userId    the user id
     * @param type      the type
     * @param ipAddress the ip address
     */
    @Override
    public void createLog(Long userId, Integer type, String ipAddress) {
        UserLogoutLog log = new UserLogoutLog();
        log.setUser(userId);
        log.setType(type);
        log.setIpAddress(ipAddress);

        userLogoutLogRepository.save(log);
    }
}
