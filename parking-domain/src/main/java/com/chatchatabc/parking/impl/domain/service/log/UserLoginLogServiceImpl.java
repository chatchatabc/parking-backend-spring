package com.chatchatabc.parking.impl.domain.service.log;

import com.chatchatabc.parking.domain.model.log.UserLoginLog;
import com.chatchatabc.parking.domain.repository.log.UserLoginLogRepository;
import com.chatchatabc.parking.domain.service.log.UserLoginLogService;
import org.springframework.stereotype.Service;

@Service
public class UserLoginLogServiceImpl implements UserLoginLogService {
    private final UserLoginLogRepository userLoginLogRepository;

    public UserLoginLogServiceImpl(UserLoginLogRepository userLoginLogRepository) {
        this.userLoginLogRepository = userLoginLogRepository;
    }

    /**
     * Created user login log
     *
     * @param userId    the user id
     * @param ipAddress the ip address
     * @param type      the type
     * @param success   the success
     */
    @Override
    public void createLog(Long userId, String ipAddress, Integer type, Boolean success) {
        UserLoginLog log = new UserLoginLog();
        log.setUser(userId);
        log.setIpAddress(ipAddress);
        log.setType(type);
        log.setSuccess(success);

        userLoginLogRepository.save(log);
    }
}
