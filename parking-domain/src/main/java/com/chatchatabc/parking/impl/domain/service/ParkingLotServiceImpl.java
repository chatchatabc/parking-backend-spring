package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.domain.model.Member;
import com.chatchatabc.parking.domain.model.ParkingLot;
import com.chatchatabc.parking.domain.model.file.ParkingLotImage;
import com.chatchatabc.parking.domain.repository.InvoiceRepository;
import com.chatchatabc.parking.domain.repository.MemberRepository;
import com.chatchatabc.parking.domain.repository.ParkingLotRepository;
import com.chatchatabc.parking.domain.repository.file.ParkingLotImageRepository;
import com.chatchatabc.parking.domain.service.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ParkingLotRepository parkingLotRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private ParkingLotImageRepository parkingLotImageRepository;

    /**
     * Save parking lot
     *
     * @param parkingLot the parking lot
     */
    @Override
    public void saveParkingLot(ParkingLot parkingLot) {
        parkingLotRepository.save(parkingLot);
    }

    /**
     * Register a new parking lot
     *
     * @param ownerId     the owner of the parking lot
     * @param name        the name of the parking lot
     * @param latitude    the latitude of the parking lot
     * @param longitude   the longitude of the parking lot
     * @param address     the address of the parking lot
     * @param description the description of the parking lot
     * @param capacity    the capacity of the parking lot
     * @return the parking lot
     */
    @Override
    public ParkingLot registerParkingLot(String ownerId, String name, Double latitude, Double longitude, String address, String description, Integer capacity, LocalDateTime businessHoursStart, LocalDateTime businessHoursEnd, Integer openDaysFlag) throws Exception {
        Optional<Member> owner = memberRepository.findByMemberUuid(ownerId);
        if (owner.isEmpty()) {
            throw new Exception("Member not found");
        }
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setOwner(owner.get().getId());
        parkingLot.setName(name);
        parkingLot.setLatitude(latitude);
        parkingLot.setLongitude(longitude);
        parkingLot.setAddress(address);
        parkingLot.setDescription(description);
        parkingLot.setCapacity(capacity);
        parkingLot.setAvailableSlots(capacity);
        parkingLot.setStatus(0);
        parkingLot.setBusinessHoursStart(businessHoursStart);
        parkingLot.setBusinessHoursEnd(businessHoursEnd);
        parkingLot.setOpenDaysFlag(openDaysFlag);
        return parkingLotRepository.save(parkingLot);
    }

    /**
     * Update parking lot
     *
     * @param memberUuid     the member id
     * @param parkingLotUuid the parking lot uuid
     * @param name           the name of the parking lot
     * @param latitude       the latitude of the parking lot
     * @param longitude      the longitude of the parking lot
     * @param address        the address of the parking lot
     * @param description    the description of the parking lot
     * @param capacity       the capacity of the parking lot
     * @return the parking lot
     */
    @Override
    public ParkingLot updateParkingLot(String memberUuid, String parkingLotUuid, String name, Double latitude, Double longitude, String address, String description, Integer capacity, LocalDateTime businessHoursStart, LocalDateTime businessHoursEnd, Integer openDaysFlag, List<ParkingLotImage> images) throws Exception {
        Optional<Member> member = memberRepository.findByMemberUuid(memberUuid);
        if (member.isEmpty()) {
            throw new Exception("Member not found");
        }
        Optional<ParkingLot> parkingLot = parkingLotRepository.findByParkingLotUuid(parkingLotUuid);
        if (parkingLot.isEmpty()) {
            throw new Exception("Parking lot not found");
        }

        // Apply Updates
        if (name != null) {
            parkingLot.get().setName(name);
        }
        if (latitude != null) {
            parkingLot.get().setLatitude(latitude);
        }
        if (longitude != null) {
            parkingLot.get().setLongitude(longitude);
        }
        if (address != null) {
            parkingLot.get().setAddress(address);
        }
        if (description != null) {
            parkingLot.get().setDescription(description);
        }
        if (capacity != null) {
            parkingLot.get().setCapacity(capacity);
            // Get active invoices and update available slots
            Long activeInvoices = invoiceRepository.countActiveInvoicesByParkingLotId(parkingLot.get().getId());
            parkingLot.get().setAvailableSlots(capacity - activeInvoices.intValue());
        }
        if (businessHoursStart != null) {
            parkingLot.get().setBusinessHoursStart(businessHoursStart);
        }
        if (businessHoursEnd != null) {
            parkingLot.get().setBusinessHoursEnd(businessHoursEnd);
        }
        if (openDaysFlag != null) {
            parkingLot.get().setOpenDaysFlag(openDaysFlag);
        }

        // Update Image Order
        if (images != null) {
            List<ParkingLotImage> updatedImages = new ArrayList<>();
            for (ParkingLotImage image : images) {
                Optional<ParkingLotImage> parkingLotImage = parkingLotImageRepository.findById(image.getId());
                if (parkingLotImage.isEmpty()) {
                    throw new Exception("Image not found");
                }
                parkingLotImage.get().setFileOrder(image.getFileOrder());
                updatedImages.add(parkingLotImage.get());
            }
            parkingLotImageRepository.saveAll(updatedImages);
        }

        // TODO: NATS publish parking lot update
        return parkingLotRepository.save(parkingLot.get());
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
