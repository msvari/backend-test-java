package com.fcamara.park.controller;

import com.fcamara.park.model.Address;
import com.fcamara.park.model.Spot;
import com.fcamara.park.security.SecurityFilter;
import com.fcamara.park.service.SpotService;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(SpotController.class)
@AutoConfigureMockMvc(addFilters = false)
class SpotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SpotService spotService;

    @MockBean
    private SecurityFilter securityFilter;

    @InjectMocks
    private SpotController spotController;


    @Test
    @DisplayName("Test bad request save spot")
    void testBadRequestSaveSpot() throws Exception {

        UUID spotId = UUID.randomUUID();

        var address = createAddressJson("STREET", "654", "COMPLEMENT", "65143278", "CITY", "STATE", "COUNTRY");
        var spot = createSpotJson("SPOT NAME", "8716348726348", "8152732567", address, 23, 12);

        mockMvc.perform(MockMvcRequestBuilders.post("/spots", spotId)
                        .param("name", "SPOT NAME")
                        .param("cnpj", "8716348726348")
                        .param("phoneNumber", "8152732567")
                        .param("carParkingSpaces", "23")
                        .param("motorcycleParkingSpaces", "12")
                .contentType(MediaType.APPLICATION_JSON)
                .content(spot)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verifyNoInteractions(spotService);

    }

    @Test
    @DisplayName("Test not found find spots")
    void testNotFoundFindSpots() throws Exception {
        UUID spotId = UUID.randomUUID();
        Spot mockSpot = createSpot(spotId, "SPOT NAME", "8716348726348", "8152732567", 23, 12,
                "STREET", "654", "COMPLEMENT", "65143278", "CITY", "STATE", "COUNTRY");

        when(spotService.findById(spotId)).thenReturn(Optional.of(mockSpot));

        mockMvc.perform(MockMvcRequestBuilders.get("/spots", spotId)
                        .param("name", anyString())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(spotService).findSpot(anyString());
    }

    @Test
    @DisplayName("Test success delete spot")
    void testSuccessDeleteSpot() throws Exception {
        UUID spotId = UUID.randomUUID();
        Spot mockSpot = createSpot(spotId, "SPOT NAME", "8716348726348", "8152732567", 23, 12,
                "STREET", "654", "COMPLEMENT", "65143278", "CITY", "STATE", "COUNTRY");

        when(spotService.findById(spotId)).thenReturn(Optional.of(mockSpot));

        mockMvc.perform(MockMvcRequestBuilders.delete("/spots/{id}", spotId)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(spotService).findById(spotId);
        Mockito.verify(spotService).delete(mockSpot);
    }

    @Test
    @DisplayName("Test not found delete spot")
    void testNotFoundDeleteSpot() throws Exception {
        UUID spotId = UUID.randomUUID();

        when(spotService.findById(spotId)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/spots/{id}", spotId)
        ).andExpect(status().isNotFound());

        Mockito.verify(spotService).findById(spotId);
        Mockito.verifyNoMoreInteractions(spotService);
    }

    @Test
    @DisplayName("Test fail delete spot")
    void testFailDeleteSpot() throws Exception {
        UUID spotId = UUID.randomUUID();
        Spot mockSpot = new Spot();

        when(spotService.findById(spotId)).thenReturn(Optional.of(mockSpot));
        doThrow(new RuntimeException("Database error")).when(spotService).delete(mockSpot);

        mockMvc.perform(delete("/spots/{id}", spotId))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Um erro interno ocorreu"));
    }

    private Spot createSpot(UUID spotId, String name, String cnpj, String phoneNumber, int numberCarSpaces, int numberMotorcycleSpaces,
                            String street, String number, String complement, String zipCode, String city, String state, String country) {
        var newAddress = Address.builder()
                .street(street)
                .number(number)
                .complement(complement)
                .zipCode(zipCode)
                .city(city)
                .state(state)
                .country(country)
                .build();

        var spot = Spot.builder()
                .id(spotId)
                .name(name)
                .cnpj(cnpj)
                .address(newAddress)
                .phoneNumber(phoneNumber)
                .carParkingSpaces(numberCarSpaces)
                .motorcycleParkingSpaces(numberMotorcycleSpaces)
                .updateDate(LocalDateTime.now(ZoneId.of("UTC")))
                .createDate(LocalDateTime.now(ZoneId.of("UTC")))
                .build();
        return spot;
    }

    private String createSpotJson(String name, String cnpj, String phoneNumber, String address, int numberCarSpaces, int numberMotorcycleSpaces) {
        return String.format("""
                {
                    "name": "%s",
                    "cnpj": "%s",
                    "phoneNumber": "%s",
                    "address": "%s",
                    "carParkingSpaces": "%s",
                    "motorcycleParkingSpaces": "%s"
                }
                """, name, cnpj, phoneNumber, address, numberCarSpaces, numberMotorcycleSpaces);
    }

    private String createAddressJson(String street, String number, String complement, String zipCode, String city, String state, String country) {
        return String.format("""
                {
                    "street": "%s",
                    "number": "%s",
                    "complement": "%s",
                    "zipCode": "%s",
                    "city": "%s",
                    "state": "%s",
                    "country": "%s"
                }
                """, street, number, complement, zipCode, city, state, country);
    }

}