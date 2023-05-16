package com.chatchatabc.parking.domain.service.log;

public interface MemberLoginLogService {

    /**
     * Created member login log
     *
     * @param memberId  the member id
     * @param ipAddress the ip address
     * @param type      the type
     * @param success   the success
     */
    void createdLog(Long memberId, String ipAddress, Integer type, Boolean success);
}
