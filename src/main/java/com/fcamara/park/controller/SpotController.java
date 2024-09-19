package com.fcamara.park.controller;

import com.fcamara.park.model.Address;
import com.fcamara.park.model.Spot;
import com.fcamara.park.service.SpotService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/spots")
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
@Slf4j
public class SpotController {

    private final SpotService spotService;

    @PostMapping
    public ResponseEntity<Object> saveSpot(@RequestParam String name,
                                           @RequestParam @Pattern(regexp = "(^\\d{2})\\d{12}$") String cnpj,
                                           @RequestBody Address address,
                                           @RequestParam String phoneNumber,
                                           @RequestParam @Min(0) @Max(9999) long carParkingSpaces,
                                           @RequestParam @Min(0) @Max(9999) long motorcycleParkingSpaces) {

        log.info("state=init-create-spot , name={}, cnpj={}", name, cnpj);
        try {
            var newAddress = Address.builder()
                    .street(address.getStreet())
                    .number(address.getNumber())
                    .complement(address.getComplement())
                    .zipCode(address.getZipCode())
                    .city(address.getCity())
                    .state(address.getState())
                    .country(address.getCountry())
                    .build();

            var spot = Spot.builder()
                    .name(name)
                    .cnpj(cnpj)
                    .address(newAddress)
                    .phoneNumber(phoneNumber)
                    .carParkingSpaces(carParkingSpaces)
                    .motorcycleParkingSpaces(motorcycleParkingSpaces)
                    .updateDate(LocalDateTime.now(ZoneId.of("UTC")))
                    .createDate(LocalDateTime.now(ZoneId.of("UTC")))
                    .build();
            spotService.save(spot);
            log.info("state=end-success-create-spot , name={}, cnpj={}", name, cnpj);

            return ResponseEntity.status(HttpStatus.CREATED).body(spot);
        } catch (Exception e) {
            log.error("state=error-create-spot, name={}, cnpj={}", name, cnpj, e);
            return ResponseEntity.internalServerError().body("Um erro interno ocorreu");
        }

    }

    @GetMapping
    public ResponseEntity<List<Spot>> getSpots(
            @RequestParam(value = "name", required = false) String name) {

        log.info("state=init-find-spots , name={}", name);
        try {
            List<Spot> spots = spotService.findSpot(name);

            if (spots.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>());
            }
            log.info("state=end-success-find-spots , name={} ", name);

            return ResponseEntity.ok(spots);
        } catch (Exception e) {
            log.error("state=error-find-spots, name={}", name , e);
            return ResponseEntity.internalServerError().body(new ArrayList<>());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteSpot(@PathVariable(value = "id") UUID id) {

        log.info("state=init-delete-spot , id={}", id);
        try {
            Optional<Spot> spot = spotService.findById(id);
            if(spot.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estacionamento não encontrado");
            }

            spotService.delete(spot.get());
            log.info("state=end-success-delete-spot , id={} ", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("state=error-delete-spot, id={}", id , e);
            return ResponseEntity.internalServerError().body("Um erro interno ocorreu");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateSpot(@PathVariable(value = "id") UUID id,
                                             @RequestParam String name,
                                             @RequestParam @Pattern(regexp = "(^\\d{2})\\d{12}$") String cnpj,
                                             @RequestBody Address address,
                                             @RequestParam String phoneNumber,
                                             @RequestParam @Min(0) @Max(9999) long carParkingSpaces,
                                             @RequestParam @Min(0) @Max(9999) long motorcycleParkingSpaces) {

        log.info("state=init-update-spot , id={}", id);
        try {
            Optional<Spot> spot = spotService.findById(id);
            if(spot.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estacionamento não encontrado");
            }

            var updatedSpot = spot.get();
            updatedSpot.setName(name);
            updatedSpot.setCnpj(cnpj);
            updatedSpot.setAddress(address);
            updatedSpot.setPhoneNumber(phoneNumber);
            updatedSpot.setCarParkingSpaces(carParkingSpaces);
            updatedSpot.setMotorcycleParkingSpaces(motorcycleParkingSpaces);
            updatedSpot.setUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            spotService.save(updatedSpot);
            log.info("state=end-success-update-spot , id={} ", id);

            return ResponseEntity.status(HttpStatus.OK).body(updatedSpot);
        } catch (Exception e) {
            log.error("state=error-update-spot, id={}", id , e);
            return ResponseEntity.internalServerError().body("Um erro interno ocorreu");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneSpot(@PathVariable(value = "id") UUID id) {
        log.info("state=init-find-spot , id={}", id);
        try {
            Optional<Spot> spot = spotService.findById(id);
            log.info("state=end-success-find-spot-by-id , id={} ", id);
            return spot.<ResponseEntity<Object>>map(item -> ResponseEntity.status(HttpStatus.OK).body(item)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estacionamento não encontrado."));
        } catch (Exception e) {
            log.error("state=error-find-spot, id={}", id , e);
            return ResponseEntity.internalServerError().body("Um erro interno ocorreu");
        }
    }

    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<Object> getSpotByCnpj(@PathVariable(value = "cnpj") String cnpj) {
        log.info("state=init-find-spot-by-cnpj , cnpj={}", cnpj);
        try {
            Optional<Spot> spot = spotService.findByCnpj(cnpj);
            log.info("state=end-success-find-spot-by-cjpj , cnpj={} ", cnpj);
            return spot.<ResponseEntity<Object>>map(item -> ResponseEntity.status(HttpStatus.OK).body(item)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estacionamento não encontrado."));
        } catch (Exception e) {
            log.error("state=error-find-spot-by-cnpj, cnpj={}", cnpj , e);
            return ResponseEntity.internalServerError().body("Um erro interno ocorreu");
        }
    }

}
