package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.domain.model.Vehicle
import com.chatchatabc.parking.domain.repository.VehicleRepository
import com.chatchatabc.parking.domain.service.VehicleService
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@WebMvcTest(controllers = [VehicleController::class])
class VehicleControllerTest : AuthorizedBaseTest() {
    @Autowired
    lateinit var mvc: MockMvc

    @MockBean
    lateinit var vehicleRepository: VehicleRepository

    @MockBean
    lateinit var vehicleService: VehicleService

    @Test
    fun testGetVehicleById() {
        val vehicle = Vehicle().apply {
            id = 1
            owner = 1
            plateNumber = "1111"
        }
        given(vehicleRepository.findByVehicleUuid("1")).willReturn(Optional.of(vehicle))
        val result = this.mvc.perform(get("/api/vehicle/get/1"))
            .andExpect(status().isOk()).andReturn()
    }
}