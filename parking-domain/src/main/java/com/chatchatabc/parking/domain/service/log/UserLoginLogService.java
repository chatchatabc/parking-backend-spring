package com.chatchatabc.parking.domain.service.log;

public interface UserLoginLogService {

    /**
     * Created user login log
     *
     * @param userId    the user id
     * @param ipAddress the ip address
     * @param type      the type
     * @param success   the success
     */
    void createLog(Long userId, String ipAddress, Integer type, Boolean success);
}
