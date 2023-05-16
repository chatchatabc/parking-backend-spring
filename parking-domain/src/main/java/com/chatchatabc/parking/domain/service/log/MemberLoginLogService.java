package com.chatchatabc.parking.domain.service.log;

import com.chatchatabc.parking.domain.model.Member;

public interface MemberLoginLogService {

    /**
     * Created member login log
     *
     * @param member    the member
     * @param ipAddress the ip address
     * @param type      the type
     * @param success   the success
     */
    void createdLog(Member member, String ipAddress, Integer type, Boolean success);
}
