package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.domain.model.ParkingLot;
import com.chatchatabc.parking.domain.model.User;
import com.chatchatabc.parking.domain.repository.InvoiceRepository;
import com.chatchatabc.parking.domain.repository.ParkingLotRepository;
import com.chatchatabc.parking.domain.repository.UserRepository;
import com.chatchatabc.parking.domain.service.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ParkingLotRepository parkingLotRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;

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
        Optional<User> owner = userRepository.findByUserId(ownerId);
        if (owner.isEmpty()) {
            throw new Exception("User not found");
        }
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setOwner(owner.get());
        parkingLot.setName(name);
        parkingLot.setLatitude(latitude);
        parkingLot.setLongitude(longitude);
        parkingLot.setAddress(address);
        parkingLot.setDescription(description);
        parkingLot.setCapacity(capacity);
        parkingLot.setAvailableSlots(capacity);
        parkingLot.setDraft(true);
        parkingLot.setBusinessHoursStart(businessHoursStart);
        parkingLot.setBusinessHoursEnd(businessHoursEnd);
        parkingLot.setOpenDaysFlag(openDaysFlag);
        // TODO: Add owner to user_parking_lot table
        return parkingLotRepository.save(parkingLot);
    }

    /**
     * Update parking lot
     *
     * @param userId       the user id
     * @param parkingLotId the parking lot id
     * @param name         the name of the parking lot
     * @param latitude     the latitude of the parking lot
     * @param longitude    the longitude of the parking lot
     * @param address      the address of the parking lot
     * @param description  the description of the parking lot
     * @param capacity     the capacity of the parking lot
     * @return the parking lot
     */
    @Override
    public ParkingLot updateParkingLot(String userId, String parkingLotId, String name, Double latitude, Double longitude, String address, String description, Integer capacity, LocalDateTime businessHoursStart, LocalDateTime businessHoursEnd, Integer openDaysFlag) throws Exception {
        // TODO: Get user from allowed list to check for permission
        Optional<User> user = userRepository.findByUserId(userId);
        if (user.isEmpty()) {
            throw new Exception("User not found");
        }
        Optional<ParkingLot> parkingLot = parkingLotRepository.findById(parkingLotId);
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
            Long activeInvoices = invoiceRepository.countActiveInvoicesByParkingLotId(parkingLotId);
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
        // TODO: NATS publish parking lot update
        return parkingLotRepository.save(parkingLot.get());
    }

    /**
     * Verify parking lot
     *
     * @param userId       the user id
     * @param parkingLotId the parking lot id
     * @return the parking lot
     */
    @Override
    public ParkingLot verifyParkingLot(String userId, String parkingLotId) throws Exception {
        Optional<User> user = userRepository.findByUserId(userId);
        if (user.isEmpty()) {
            throw new Exception("User not found");
        }
        Optional<ParkingLot> parkingLot = parkingLotRepository.findById(parkingLotId);
        if (parkingLot.isEmpty()) {
            throw new Exception("Parking lot not found");
        }
        // Check if parking lot is already verified
        if (parkingLot.get().getVerifiedAt() != null) {
            throw new Exception("Parking lot is already verified");
        }
        parkingLot.get().setVerifiedAt(LocalDateTime.now());
        parkingLot.get().setDraft(false);
        parkingLot.get().setPending(false);
        return parkingLotRepository.save(parkingLot.get());
    }
}
