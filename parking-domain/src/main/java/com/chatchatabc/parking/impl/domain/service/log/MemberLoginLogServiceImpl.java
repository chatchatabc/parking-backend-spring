package com.chatchatabc.parking.impl.domain.service.log;

import com.chatchatabc.parking.domain.model.Member;
import com.chatchatabc.parking.domain.model.log.MemberLoginLog;
import com.chatchatabc.parking.domain.repository.log.MemberLoginLogRepository;
import com.chatchatabc.parking.domain.service.log.MemberLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberLoginLogServiceImpl implements MemberLoginLogService {
    @Autowired
    MemberLoginLogRepository memberLoginLogRepository;

    /**
     * Created member login log
     *
     * @param member    the member
     * @param ipAddress the ip address
     * @param type      the type
     * @param success   the success
     */
    @Override
    public void createdLog(Member member, String ipAddress, Integer type, Boolean success) {
        MemberLoginLog log = new MemberLoginLog();
        log.setMember(member);
        log.setIpAddress(ipAddress);
        log.setType(type);
        log.setSuccess(success);

        memberLoginLogRepository.save(log);
    }
}
