package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.domain.model.Member;
import com.chatchatabc.parking.domain.model.Vehicle;
import com.chatchatabc.parking.domain.repository.MemberRepository;
import com.chatchatabc.parking.domain.repository.VehicleRepository;
import com.chatchatabc.parking.domain.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VehicleServiceImpl implements VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private MemberRepository memberRepository;

    /**
     * Register vehicle
     *
     * @param memberId    the member id
     * @param name        the name of the vehicle
     * @param plateNumber the plate number of the vehicle
     * @param type        the type of the vehicle
     * @return the vehicle
     */
    @Override
    public Vehicle registerVehicle(String memberId, String name, String plateNumber, int type) throws Exception {
        Optional<Member> owner = memberRepository.findByMemberId(memberId);
        if (owner.isEmpty()) {
            throw new Exception("Member not found");
        }
        Vehicle vehicle = new Vehicle();
        vehicle.setName(name);
        vehicle.setPlateNumber(plateNumber);
        vehicle.setType(type);
        vehicle.setOwner(owner.get());
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        // Add member to member vehicles table
        owner.get().getVehicles().add(savedVehicle);
        memberRepository.save(owner.get());
        return savedVehicle;
    }

    /**
     * Update vehicle
     *
     * @param memberId    the member id
     * @param vehicleId   the vehicle id
     * @param name        the name of the vehicle
     * @param plateNumber the plate number of the vehicle
     * @param type        the type of the vehicle
     * @return the vehicle
     */
    @Override
    public Vehicle updateVehicle(String memberId, String vehicleId, String name, String plateNumber, Integer type) throws Exception {
        Optional<Member> member = memberRepository.findByMemberId(memberId);
        if (member.isEmpty()) {
            throw new Exception("Member not found");
        }
        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        if (vehicle.isEmpty()) {
            throw new Exception("Vehicle not found");
        }
        // Check if member has access to update vehicle
        if (!vehicle.get().getMembers().contains(member.get())) {
            throw new Exception("Vehicle not found");
        }

        // Apply changes
        if (name != null) {
            vehicle.get().setName(name);
        }
        if (plateNumber != null) {
            vehicle.get().setPlateNumber(plateNumber);
        }
        if (type != null) {
            vehicle.get().setType(type);
        }

        return vehicleRepository.save(vehicle.get());
    }

    /**
     * Add a member to a vehicle
     *
     * @param memberId      the member id
     * @param vehicleId     the vehicle id
     * @param memberToAddId the member to add id
     * @return the vehicle
     * @throws Exception the exception
     */
    @Override
    public Vehicle addMemberToVehicle(String memberId, String vehicleId, String memberToAddId) throws Exception {
        Optional<Member> member = memberRepository.findByMemberId(memberId);
        if (member.isEmpty()) {
            throw new Exception("Member not found");
        }

        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        if (vehicle.isEmpty()) {
            throw new Exception("Vehicle not found");
        }
        // Check if member has access to update vehicle
        if (!vehicle.get().getMembers().contains(member.get())) {
            throw new Exception("Vehicle not found");
        }

        Optional<Member> memberToAdd = memberRepository.findByMemberId(memberToAddId);
        if (memberToAdd.isEmpty()) {
            throw new Exception("Member to add not found");
        }
        // Check if member to add is already in vehicle
        if (vehicle.get().getMembers().contains(memberToAdd.get())) {
            throw new Exception("Member already in vehicle");
        }

        // Add vehicle to member
        memberToAdd.get().getVehicles().add(vehicle.get());
        memberRepository.save(memberToAdd.get());

        return vehicle.get();
    }

    /**
     * Remove a member from a vehicle
     *
     * @param memberId         the member id
     * @param vehicleId        the vehicle id
     * @param memberToRemoveId the member to remove id
     * @return the vehicle
     * @throws Exception the exception
     */
    @Override
    public Vehicle removeMemberFromVehicle(String memberId, String vehicleId, String memberToRemoveId) throws Exception {
        Optional<Member> member = memberRepository.findByMemberId(memberId);
        if (member.isEmpty()) {
            throw new Exception("Member not found");
        }

        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        if (vehicle.isEmpty()) {
            throw new Exception("Vehicle not found");
        }
        // Check if member has access to update vehicle
        if (!vehicle.get().getMembers().contains(member.get())) {
            throw new Exception("Vehicle not found");
        }
        // Check if member is the owner, do not remove
        if (vehicle.get().getOwner().getId().equals(memberToRemoveId)) {
            throw new Exception("Cannot remove owner");
        }

        Optional<Member> memberToRemove = memberRepository.findByMemberId(memberToRemoveId);
        if (memberToRemove.isEmpty()) {
            throw new Exception("Member to add not found");
        }
        // Check if member to remove is not in vehicle
        if (!vehicle.get().getMembers().contains(memberToRemove.get())) {
            throw new Exception("Member not in vehicle");
        }

        // Remove vehicle from member
        memberToRemove.get().getVehicles().remove(vehicle.get());
        memberRepository.save(memberToRemove.get());

        return vehicle.get();
    }
}
