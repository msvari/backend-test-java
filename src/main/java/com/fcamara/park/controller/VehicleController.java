package com.fcamara.park.controller;

import com.fcamara.park.model.Vehicle;
import com.fcamara.park.model.enumeration.Color;
import com.fcamara.park.model.enumeration.VehicleBrand;
import com.fcamara.park.model.enumeration.VehicleType;
import com.fcamara.park.service.VehicleService;
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
@RequestMapping("/vehicles")
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
@Slf4j
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<Object> saveVehicle(@RequestParam VehicleBrand brand,
                                              @RequestParam String model,
                                              @RequestParam Color color,
                                              @RequestParam @Pattern(regexp = "^[A-Za-z]{3}[A-Za-z0-9]{4}$") String licencePlate,
                                              @RequestParam VehicleType vehicleType) {

        log.info("state=init-create-vehicle , brand={}, type={}", brand, vehicleType);
        try {
            Optional<Vehicle> vehicle = vehicleService.findByLicencePlate(licencePlate.toUpperCase());
            if (vehicle.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Placa já cadastrada");
            }

            var newVehicle = Vehicle.builder()
                    .licencePlate(licencePlate)
                    .model(model)
                    .color(color)
                    .brand(brand)
                    .type(vehicleType)
                    .createDate(LocalDateTime.now(ZoneId.of("UTC")))
                    .updateDate(LocalDateTime.now(ZoneId.of("UTC")))
                    .build();
            vehicleService.save(newVehicle);
            log.info("state=end-success-create-vehicle , brand={}, type={}", brand, vehicleType);

            return ResponseEntity.status(HttpStatus.CREATED).body(newVehicle);
        } catch (Exception e) {
            log.error("state=error-create-vehicle, brand={}, type={}", brand, vehicleType, e);
            return ResponseEntity.internalServerError().body("Um erro interno ocorreu");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteVehicle(@PathVariable(value = "id") UUID id) {

        log.info("state=init-delete-vehicle , id={}", id);
        try {
            Optional<Vehicle> vehicleOptional = vehicleService.findById(id);
            if(vehicleOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veículo não encontrado");
            }

            vehicleService.delete(vehicleOptional.get());
            log.info("state=end-success-delete-vehicle , id={}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("state=error-delete-vehicle, id={}", id , e);
            return ResponseEntity.internalServerError().body("Um erro interno ocorreu");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateVehicle(@PathVariable(value = "id") UUID id,
                                                @RequestParam VehicleBrand brand,
                                                @RequestParam String model,
                                                @RequestParam Color color,
                                                @RequestParam String licencePlate,
                                                @RequestParam VehicleType vehicleType) {

        log.info("state=init-update-vehicle , id={}", id);
        try {
            Optional<Vehicle> vehicle = vehicleService.findById(id);
            if(vehicle.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veículo não encontrado");
            }

            var updatedVehicle = vehicle.get();
            updatedVehicle.setBrand(brand);
            updatedVehicle.setModel(model);
            updatedVehicle.setType(vehicleType);
            updatedVehicle.setColor(color);
            updatedVehicle.setLicencePlate(licencePlate.toUpperCase());
            updatedVehicle.setUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            vehicleService.save(updatedVehicle);
            log.info("state=end-success-update-vehicle , id={}", id);

            return ResponseEntity.status(HttpStatus.OK).body(updatedVehicle);
        } catch (Exception e) {
            log.error("state=error-update-vehicle, id={}", id , e);
            return ResponseEntity.internalServerError().body("Um erro interno ocorreu");
        }
    }

    @GetMapping
    public ResponseEntity<List<Vehicle>> getVehicles(
            @RequestParam(required = false) VehicleBrand brand,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) Color color,
            @RequestParam(required = false) VehicleType type) {

        log.info("state=init-find-vehicles , brand={}, type={}", brand, type);
        try {
            List<Vehicle> vehicles = vehicleService.findVehicles(brand, model, color, type);

            if (vehicles.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>());
            }
            log.info("state=end-success-find-vehicles , brand={}, type={}", brand, type);

            return ResponseEntity.ok(vehicles);
        } catch (Exception e) {
            log.error("state=error-find-vehicles, brand={}, type={}", brand, type , e);
            return ResponseEntity.internalServerError().body(new ArrayList<>());
        }

    }

    @GetMapping("/{licensePlate}")
    public ResponseEntity<Object> getVehicleByLicensePlate(@PathVariable(value = "licensePlate") String licensePlate) {
        log.info("state=init-find-vehicle-by-licence-plate , licence-plate={}", licensePlate);
        try {
            Optional<Vehicle> vehicleModelOptional = vehicleService.findByLicencePlate(licensePlate.toUpperCase());
            log.info("state=end-success-find-vehicle-by-licence-plate , licence-plate={}", licensePlate);
            return vehicleModelOptional.<ResponseEntity<Object>>map(vehicle -> ResponseEntity.status(HttpStatus.OK).body(vehicle)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veículo não encontrado."));
        } catch (Exception e) {
            log.error("state=error-find-vehicle-by-licence-plate, licence-plate={}", licensePlate , e);
            return ResponseEntity.internalServerError().body("Um erro interno ocorreu");
        }
    }

}
