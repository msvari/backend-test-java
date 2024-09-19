package com.fcamara.park.controller;

import com.fcamara.park.model.Vehicle;
import com.fcamara.park.model.enumeration.VehicleBrand;
import com.fcamara.park.model.enumeration.VehicleType;
import com.fcamara.park.model.enumeration.Color;
import com.fcamara.park.security.SecurityFilter;
import com.fcamara.park.service.VehicleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VehicleController.class)
@AutoConfigureMockMvc(addFilters = false)
class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehicleService vehicleService;

    @MockBean
    private SecurityFilter securityFilter;

    @InjectMocks
    private VehicleController vehicleController;

    @Test
    @DisplayName("Test not found find vehicles")
    void testNotFoundFindVehicles() throws Exception {
        UUID vehicleId = UUID.randomUUID();
        Vehicle mockVehicle = createVehicle(vehicleId);

        when(vehicleService.findById(vehicleId)).thenReturn(Optional.of(mockVehicle));

        mockMvc.perform(MockMvcRequestBuilders.get("/vehicles", vehicleId)
                .param("brand", anyString())
                .param("model", anyString())
                .param("color", anyString())
                .param("type", anyString())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Test success delete vehicle")
    void testSuccesDeleteVehicle() throws Exception {
        UUID vehicleId = UUID.randomUUID();
        Vehicle mockVehicle = createVehicle(vehicleId);

        when(vehicleService.findById(vehicleId)).thenReturn(Optional.of(mockVehicle));

        mockMvc.perform(MockMvcRequestBuilders.delete("/vehicles/{id}", vehicleId)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(vehicleService).findById(vehicleId);
        Mockito.verify(vehicleService).delete(mockVehicle);
    }

    @Test
    @DisplayName("Test not found delete vehicle")
    void testNotFoundDeleteVehicle() throws Exception {
        UUID vehicleId = UUID.randomUUID();

        when(vehicleService.findById(vehicleId)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/vehicles/{id}", vehicleId)
        ).andExpect(status().isNotFound());

        Mockito.verify(vehicleService).findById(vehicleId);
        Mockito.verifyNoMoreInteractions(vehicleService);
    }

    @Test
    @DisplayName("Test fail delete vehicle")
    void testFailDeleteVehicle() throws Exception {
        UUID vehicleId = UUID.randomUUID();
        Vehicle mockVehicle = new Vehicle();

        when(vehicleService.findById(vehicleId)).thenReturn(Optional.of(mockVehicle));
        doThrow(new RuntimeException("Database error")).when(vehicleService).delete(mockVehicle);

        mockMvc.perform(delete("/vehicles/{id}", vehicleId))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Um erro interno ocorreu"));
    }

    private Vehicle createVehicle(UUID vehicleId) {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);
        vehicle.setBrand(VehicleBrand.FIAT);
        vehicle.setModel("Cronos");
        vehicle.setColor(Color.GREEN);
        vehicle.setLicencePlate("OTA5162");
        vehicle.setType(VehicleType.CAR);
        return vehicle;
    }

}