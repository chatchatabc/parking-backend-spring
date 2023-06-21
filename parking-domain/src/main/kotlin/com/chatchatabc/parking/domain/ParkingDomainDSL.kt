package com.chatchatabc.parking.domain

import com.chatchatabc.parking.domain.model.*

val String.user: User
    get() {
        // TODO: Add custom errors
        if (this.contains("@")) {
            return SpringContextUtils.getUserRepository().findByEmail(this).orElseThrow()
        } else if (this.contains("-")) {
            return SpringContextUtils.getUserRepository().findByUserUuid(this).orElseThrow()
        } else if (this.matches(Regex("^\\+[0-9]\\d{1,14}\$"))) {
            return SpringContextUtils.getUserRepository().findByPhone(this).orElseThrow()
        }
        return SpringContextUtils.getUserRepository().findByUsername(this).orElseThrow()
    }

val Long.user: User
    get() {
        // TODO: Add custom errors
        return SpringContextUtils.getUserRepository().findById(this).orElseThrow()
    }

val String.vehicle: Vehicle
    get() {
        // TODO: Add custom errors
        // if string is uuid
        if (this.length == 36) {
            return SpringContextUtils.getVehicleRepository().findByVehicleUuid(this).orElseThrow()
        }
        return SpringContextUtils.getVehicleRepository().findByPlateNumber(this).orElseThrow()
    }

val String.vehicleBrand: VehicleBrand
    get() {
        // TODO: Add custom errors
        return SpringContextUtils.getVehicleBrandRepository().findByBrandUuid(this).orElseThrow()
    }

val String.vehicleType: VehicleType
    get() {
        // TODO: Add custom errors
        return SpringContextUtils.getVehicleTypeRepository().findByTypeUuid(this).orElseThrow()
    }

val String.vehicleModel: VehicleModel
    get() {
        // TODO: Add custom errors
        return SpringContextUtils.getVehicleModelRepository().findByModelUuid(this).orElseThrow()
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

val String.rate: Rate
    get() {
        // TODO: Add custom errors
        return SpringContextUtils.getRateRepository().findById(this).orElseThrow()
    }

val String.rateByParkingLot: Rate
    get() {
        // TODO: Add custom errors
        return SpringContextUtils.getParkingLotRepository().findByParkingLotUuid(this).orElseThrow().getRate()
    }

val String.invoice: Invoice
    get() {
        // TODO: Add custom errors
        return SpringContextUtils.getInvoiceRepository().findByInvoiceUuid(this).orElseThrow()
    }

val Long.report: Report
    get() {
        // TODO: Add custom errors
        return SpringContextUtils.getReportRepository().findById(this).orElseThrow()
    }

val String.jeepney: Jeepney
    get() {
        // TODO: Add custom errors
        if (this.length == 36) {
            return SpringContextUtils.getJeepneyRepository().findByJeepneyUuid(this).orElseThrow()
        } else if (this.contains("-")) {
            SpringContextUtils.getJeepneyRepository().findByPlateNumber(this).orElseThrow()
        }
        return SpringContextUtils.getJeepneyRepository().findByName(this).orElseThrow()
    }

val String.route: Route
    get() {
        // TODO: Add custom errors
        return SpringContextUtils.getRouteRepository().findByRouteUuid(this).orElseThrow()
    }

val Long.routeEdges: List<RouteEdge>
    get() {
        // TODO: Add custom errors
        return SpringContextUtils.getRouteEdgeRepository().findAllByRouteId(this)
    }

val Long.routeEdge: RouteEdge
    get() {
        // TODO: Add custom errors
        return SpringContextUtils.getRouteEdgeRepository().findById(this).orElseThrow()
    }

val Long.routeNode: RouteNode
    get() {
        // TODO: Add custom errors
        return SpringContextUtils.getRouteNodeRepository().findById(this).orElseThrow()
    }