package com.chatchatabc.parking.domain.service.log;

public interface UserLogoutLogService {

    /**
     * Create user logout log
     *
     * @param userId    the user id
     * @param type      the type
     * @param ipAddress the ip address
     */
    void createLog(Long userId, Integer type, String ipAddress);
}
