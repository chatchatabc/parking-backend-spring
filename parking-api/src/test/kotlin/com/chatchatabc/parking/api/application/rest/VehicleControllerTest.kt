package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.domain.model.Vehicle
import com.chatchatabc.parking.domain.repository.VehicleRepository
import com.chatchatabc.parking.domain.service.VehicleService
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@WebMvcTest(controllers = [VehicleController::class])
class VehicleControllerTest : AuthorizedBaseTest() {
    @MockBean
    lateinit var vehicleRepository: VehicleRepository

    @MockBean
    lateinit var vehicleService: VehicleService

    @Test
    fun testGetVehicleById() {
        val vehicle = Vehicle().apply {
            this.id = 1
            this.vehicleUuid = "1"
            this.owner = 1
            this.plateNumber = "xxx-1111"
        }

        given(vehicleRepository.findByVehicleUuid("1")).willReturn(Optional.of(vehicle))

        val result = this.mvc.perform(get("/api/vehicle/1"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk()).andReturn()
    }

    @Test
    fun testGetMyVehicles() {
        val pr = PageRequest.of(0, 10)

        given(vehicleRepository.findAllByUser("1", pr)).willReturn(Page.empty())

        val result = this.mvc.perform(get("/api/vehicle/my-vehicles"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk()).andReturn()
    }
}