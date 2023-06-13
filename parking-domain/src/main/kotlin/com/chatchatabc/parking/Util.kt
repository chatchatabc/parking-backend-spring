package com.chatchatabc.parking

import com.chatchatabc.parking.domain.SpringContextUtils
import com.chatchatabc.parking.domain.model.ParkingLot
import com.chatchatabc.parking.domain.model.User

val String.user: User
    get() {
        // TODO: Add custom errors
        if (this.contains("@")) {
            return SpringContextUtils.getUserRepository().findByEmail(this).orElseThrow()
        } else if (this.contains("-")) {
            return SpringContextUtils.getUserRepository().findByUserUuid(this).orElseThrow()
        } else if (this.matches(Regex("^\\+?[0-9]\\d{1,14}\$"))) {
            return SpringContextUtils.getUserRepository().findByPhone(this).orElseThrow()
        }
        return SpringContextUtils.getUserRepository().findByUsername(this).orElseThrow()
    }

val String.parkingLot: ParkingLot
    get() {
        // TODO: Add custom errors
        return SpringContextUtils.getParkingLotRepository().findByParkingLotUuid(this).orElseThrow()
    }

val Long.parkingLot: ParkingLot
    get() {
        // TODO: Add custom errors
        return SpringContextUtils.getParkingLotRepository().findById(this).orElseThrow()
    }

val String.parkingLotByOwner: ParkingLot
    get() {
        // TODO: Add custom errors
        return SpringContextUtils.getParkingLotRepository().findByOwnerUuid(this).orElseThrow()
    }

val Long.parkingLotByOwner: ParkingLot
    get() {
        // TODO: Add custom errors
        return SpringContextUtils.getParkingLotRepository().findByOwner(this).orElseThrow()
    }