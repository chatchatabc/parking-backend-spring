package com.chatchatabc.parking.domain.service.log;

import com.chatchatabc.parking.domain.model.Member;

public interface MemberLogoutLogService {

    /**
     * Create member logout log
     *
     * @param member    the member
     * @param type      the type
     * @param ipAddress the ip address
     */
    void createLog(Member member, Integer type, String ipAddress);
}
