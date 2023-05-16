package com.chatchatabc.parking.domain.service.log;

public interface MemberLogoutLogService {

    /**
     * Create member logout log
     *
     * @param memberId  the member id
     * @param type      the type
     * @param ipAddress the ip address
     */
    void createLog(Long memberId, Integer type, String ipAddress);
}
