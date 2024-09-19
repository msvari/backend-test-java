package com.fcamara.park.controller;

import com.fcamara.park.model.Park;
import com.fcamara.park.service.SpotService;
import com.fcamara.park.service.VehicleService;
import com.fcamara.park.service.impl.ParkServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/parking")
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
@Slf4j
public class ParkController {

    private final ParkServiceImpl parkService;

    private final VehicleService vehicleService;

    private final SpotService spotService;

    @PostMapping("/getIn")
    public ResponseEntity<Object> parkVehicle(@RequestParam UUID spotId,
                                              @RequestParam String licensePlate) {

        log.info("state=init-parking , spotId={} , licencePlate={} ", spotId, licensePlate);
        try {
            var vehicle = vehicleService.findByLicencePlate(licensePlate)
                    .orElseThrow(() -> new IllegalArgumentException("Veículo não encontrado"));

            Park parkedVehicle = parkService.parkVehicle(vehicle, spotId);
            log.info("state=end-success-parking , spotId={} , licencePlate={} ", spotId, licensePlate);
            return ResponseEntity.status(HttpStatus.CREATED).body(parkedVehicle);
        } catch (IllegalArgumentException e) {
            log.info("state=error-not-found-parking , spotId={} , licencePlate={} ", spotId, licensePlate);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            log.info("state=error-not-acceptable-parking , spotId={} , licencePlate={} ", spotId, licensePlate);
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        } catch (Exception e) {
            log.info("state=error-parking , spotId={} , licencePlate={} ", spotId, licensePlate);
            return ResponseEntity.internalServerError().body("Um erro interno ocorreu");
        }
    }

    @PostMapping("/getOut")
    public ResponseEntity<Object> unparkVehicle(@RequestParam UUID spotId,
                                                @RequestParam String licensePlate) {
        log.info("state=init-unparking , spotId={} , licencePlate={} ", spotId, licensePlate);
        try {
            var vehicle = vehicleService.findByLicencePlate(licensePlate)
                    .orElseThrow(() -> new IllegalArgumentException("Veículo não encontrado"));

            Park unparkedVehicle = parkService.unparkVehicle(vehicle, spotId);
            log.info("state=end-success-unparking , spotId={} , licencePlate={} ", spotId, licensePlate);
            return ResponseEntity.status(HttpStatus.CREATED).body(unparkedVehicle);
        } catch (IllegalArgumentException e) {
            log.info("state=error-not-found-unparking , spotId={} , licencePlate={} ", spotId, licensePlate);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.info("state=error-unparking , spotId={} , licencePlate={} ", spotId, licensePlate);
            return ResponseEntity.internalServerError().body("Um erro interno ocorreu");
        }
    }

}
