package com.chatchatabc.parking.impl.domain.service.log;

import com.chatchatabc.parking.domain.model.log.MemberLogoutLog;
import com.chatchatabc.parking.domain.repository.log.MemberLogoutLogRepository;
import com.chatchatabc.parking.domain.service.log.MemberLogoutLogService;
import org.springframework.stereotype.Service;

@Service
public class MemberLogoutLogServiceImpl implements MemberLogoutLogService {
    private final MemberLogoutLogRepository memberLogoutLogRepository;

    public MemberLogoutLogServiceImpl(MemberLogoutLogRepository memberLogoutLogRepository) {
        this.memberLogoutLogRepository = memberLogoutLogRepository;
    }

    /**
     * Create member logout log
     *
     * @param memberId  the member id
     * @param type      the type
     * @param ipAddress the ip address
     */
    @Override
    public void createLog(Long memberId, Integer type, String ipAddress) {
        MemberLogoutLog log = new MemberLogoutLog();
        log.setMember(memberId);
        log.setType(type);
        log.setIpAddress(ipAddress);

        memberLogoutLogRepository.save(log);
    }
}
