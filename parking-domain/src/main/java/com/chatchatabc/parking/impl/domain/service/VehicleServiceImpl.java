package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.domain.model.Member;
import com.chatchatabc.parking.domain.model.Vehicle;
import com.chatchatabc.parking.domain.repository.MemberRepository;
import com.chatchatabc.parking.domain.repository.VehicleRepository;
import com.chatchatabc.parking.domain.service.VehicleService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final MemberRepository memberRepository;

    public VehicleServiceImpl(VehicleRepository vehicleRepository, MemberRepository memberRepository) {
        this.vehicleRepository = vehicleRepository;
        this.memberRepository = memberRepository;
    }

    /**
     * Register vehicle
     *
     * @param memberUuid  the member uuid
     * @param name        the name of the vehicle
     * @param plateNumber the plate number of the vehicle
     * @param type        the type of the vehicle
     * @return the vehicle
     */
    @Override
    public Vehicle registerVehicle(String memberUuid, String name, String plateNumber, int type) throws Exception {
        Optional<Member> owner = memberRepository.findByMemberUuid(memberUuid);
        if (owner.isEmpty()) {
            throw new Exception("Member not found");
        }
        Vehicle vehicle = new Vehicle();
        vehicle.setName(name);
        vehicle.setPlateNumber(plateNumber);
        vehicle.setType(type);
        vehicle.setOwner(owner.get().getId());
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        // Add member to member vehicles table
        owner.get().getVehicles().add(savedVehicle);
        memberRepository.save(owner.get());
        return savedVehicle;
    }

    /**
     * Update vehicle
     *
     * @param updatedVehicle the updated vehicle
     */
    @Override
    public void updateVehicle(Vehicle updatedVehicle) throws Exception {
        vehicleRepository.save(updatedVehicle);
    }

    /**
     * Add a member to a vehicle
     *
     * @param memberUuid    the member uuid
     * @param vehicleUuid   the vehicle uuid
     * @param memberToAddId the member to add id
     * @return the vehicle
     * @throws Exception the exception
     */
    @Override
    public Vehicle addMemberToVehicle(String memberUuid, String vehicleUuid, String memberToAddId) throws Exception {
        Optional<Member> member = memberRepository.findByMemberUuid(memberUuid);
        if (member.isEmpty()) {
            throw new Exception("Member not found");
        }

        Optional<Vehicle> vehicle = vehicleRepository.findByVehicleUuid(vehicleUuid);
        if (vehicle.isEmpty()) {
            throw new Exception("Vehicle not found");
        }
        // Check if member has access to update vehicle
        if (!vehicle.get().getMembers().contains(member.get())) {
            throw new Exception("Vehicle not found");
        }

        Optional<Member> memberToAdd = memberRepository.findByMemberUuid(memberToAddId);
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
     * @param memberUuid       the member uuid
     * @param vehicleUuid      the vehicle uuid
     * @param memberToRemoveId the member to remove id
     * @return the vehicle
     * @throws Exception the exception
     */
    @Override
    public Vehicle removeMemberFromVehicle(String memberUuid, String vehicleUuid, String memberToRemoveId) throws Exception {
        Optional<Member> member = memberRepository.findByMemberUuid(memberUuid);
        if (member.isEmpty()) {
            throw new Exception("Member not found");
        }

        Optional<Vehicle> vehicle = vehicleRepository.findByVehicleUuid(vehicleUuid);
        if (vehicle.isEmpty()) {
            throw new Exception("Vehicle not found");
        }
        // Check if member has access to update vehicle
        if (!vehicle.get().getMembers().contains(member.get())) {
            throw new Exception("Vehicle not found");
        }
        // Check if member is the owner, do not remove
        // TODO: Fix this since there is no relationship anymore
//        if (vehicle.get().getOwner().getId().equals(memberToRemoveId)) {
//            throw new Exception("Cannot remove owner");
//        }

        Optional<Member> memberToRemove = memberRepository.findByMemberUuid(memberToRemoveId);
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
