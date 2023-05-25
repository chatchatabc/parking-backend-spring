package com.chatchatabc.parking.impl.domain.service.log;

import com.chatchatabc.parking.domain.model.log.MemberLoginLog;
import com.chatchatabc.parking.domain.repository.log.MemberLoginLogRepository;
import com.chatchatabc.parking.domain.service.log.MemberLoginLogService;
import org.springframework.stereotype.Service;

@Service
public class MemberLoginLogServiceImpl implements MemberLoginLogService {
    private final MemberLoginLogRepository memberLoginLogRepository;

    public MemberLoginLogServiceImpl(MemberLoginLogRepository memberLoginLogRepository) {
        this.memberLoginLogRepository = memberLoginLogRepository;
    }

    /**
     * Created member login log
     *
     * @param memberId  the member id
     * @param ipAddress the ip address
     * @param type      the type
     * @param success   the success
     */
    @Override
    public void createLog(Long memberId, String ipAddress, Integer type, Boolean success) {
        MemberLoginLog log = new MemberLoginLog();
        log.setMember(memberId);
        log.setIpAddress(ipAddress);
        log.setType(type);
        log.setSuccess(success);

        memberLoginLogRepository.save(log);
    }
}
