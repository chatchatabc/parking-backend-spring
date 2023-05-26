package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.domain.model.Member;
import com.chatchatabc.parking.domain.model.ParkingLot;
import com.chatchatabc.parking.domain.repository.MemberRepository;
import com.chatchatabc.parking.domain.repository.ParkingLotRepository;
import com.chatchatabc.parking.domain.service.ParkingLotService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    private final MemberRepository memberRepository;
    private final ParkingLotRepository parkingLotRepository;

    public ParkingLotServiceImpl(MemberRepository memberRepository, ParkingLotRepository parkingLotRepository) {
        this.memberRepository = memberRepository;
        this.parkingLotRepository = parkingLotRepository;
    }

    /**
     * Save parking lot
     *
     * @param parkingLot the parking lot
     */
    @Override
    public void saveParkingLot(ParkingLot parkingLot) {
        // TODO: NATS publish parking lot update
        parkingLotRepository.save(parkingLot);
    }

    /**
     * Verify parking lot
     *
     * @param memberUuid     the member id
     * @param parkingLotUuid the parking lot uuid
     * @return the parking lot
     */
    @Override
    public ParkingLot verifyParkingLot(String memberUuid, String parkingLotUuid) throws Exception {
        Optional<Member> member = memberRepository.findByMemberUuid(memberUuid);
        if (member.isEmpty()) {
            throw new Exception("Member not found");
        }
        Optional<ParkingLot> parkingLot = parkingLotRepository.findByParkingLotUuid(parkingLotUuid);
        if (parkingLot.isEmpty()) {
            throw new Exception("Parking lot not found");
        }
        // Check if parking lot is already verified
        if (parkingLot.get().getVerifiedAt() != null) {
            throw new Exception("Parking lot is already verified");
        }
        parkingLot.get().setVerifiedAt(LocalDateTime.now());
        parkingLot.get().setStatus(2);
        return parkingLotRepository.save(parkingLot.get());
    }
}
