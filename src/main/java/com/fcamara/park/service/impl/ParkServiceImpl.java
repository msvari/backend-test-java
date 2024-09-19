package com.fcamara.park.service.impl;

import com.fcamara.park.model.Park;
import com.fcamara.park.model.Spot;
import com.fcamara.park.model.Vehicle;
import com.fcamara.park.model.enumeration.VehicleType;
import com.fcamara.park.repository.ParkRepository;
import com.fcamara.park.service.ParkService;
import com.fcamara.park.service.SpotService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service("park")
@Primary
@AllArgsConstructor
public class ParkServiceImpl implements ParkService {

    private final ParkRepository parkRepository;

    private final SpotService spotService;

    @Override
    public List<Park> findAllBySpot(Spot spot) {
        return parkRepository.findAllBySpot(spot);
    }

    @Override
    public Optional<Park> findByVehicleAndExitDateIsNull(Vehicle vehicle) {
        return parkRepository.findByVehicleAndExitDateIsNull(vehicle);
    }

    @Override
    public Optional<Park> findByVehicleAndSpotAndExitDateIsNull(Vehicle vehicle, Spot spot) {
        return parkRepository.findByVehicleAndSpotAndExitDateIsNull(vehicle, spot);
    }

    @Override
    public Long availableLimit(Spot spot, VehicleType type) {

        long availableSpaces;
        var parkeds = parkRepository.countParkedVehiclesBySpotAndType(spot, type);
        if (type == VehicleType.CAR) {
            availableSpaces = spot.getCarParkingSpaces();
        } else if (type == VehicleType.MOTORCYCLE) {
            availableSpaces = spot.getMotorcycleParkingSpaces();
        } else {
            throw new IllegalArgumentException("Tipo de veículo não encontrado");
        }

        return availableSpaces - parkeds;
    }

    @Override
    public Park parkVehicle(Vehicle vehicle, UUID spotId) {

        if (vehicle.isParked()) {
            throw new IllegalStateException("O veículo já está estacionado");
        }

        var spot = spotService.findById(spotId)
                .orElseThrow(() -> new IllegalArgumentException("Estacionamento não encontrado"));

        if (availableLimit(spot, vehicle.getType()) < 1L) {
            throw new IllegalStateException("Não há vagas disponíveis");
        }

        Park park = Park.builder()
                .vehicle(vehicle)
                .spot(spot)
                .entryDate(LocalDateTime.now(ZoneId.of("UTC")))
                .createDate(LocalDateTime.now(ZoneId.of("UTC")))
                .updateDate(LocalDateTime.now(ZoneId.of("UTC")))
                .build();

        return parkRepository.save(park);
    }

    @Override
    public Park unparkVehicle(Vehicle vehicle, UUID spotId) {

        var spot = spotService.findById(spotId)
                .orElseThrow(() -> new IllegalArgumentException("Estacionamento não encontrado"));

        var parking = parkRepository.findByVehicleAndSpotAndExitDateIsNull(vehicle, spot)
                .orElseThrow(() -> new IllegalArgumentException("Veículo não está no estacionamento"));

        parking.setExitDate(LocalDateTime.now(ZoneId.of("UTC")));
        parking.setUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return parkRepository.save(parking);
    }

}
