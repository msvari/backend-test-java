package com.fcamara.park.service.impl;

import com.fcamara.park.model.Vehicle;
import com.fcamara.park.model.enumeration.Color;
import com.fcamara.park.model.enumeration.VehicleBrand;
import com.fcamara.park.model.enumeration.VehicleType;
import com.fcamara.park.repository.ParkRepository;
import com.fcamara.park.repository.VehicleRepository;
import com.fcamara.park.service.VehicleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    VehicleRepository vehicleRepository;

    ParkRepository parkRepository;

    @Override
    public Vehicle save(@Valid Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @Override
    public Optional<Vehicle> findById(UUID id) {
        return vehicleRepository.findById(id);
    }

    @Override
    public void delete(Vehicle vehicle) {
        vehicleRepository.delete(vehicle);
    }

    @Override
    public List<Vehicle> findVehicles(VehicleBrand brand, String model, Color color, VehicleType type) {
        return vehicleRepository.findByBrandOrModelOrColorOrType(brand, model, color, type);
    }

    @Override
    public Optional<Vehicle> findByLicencePlate(String licencePlate) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findByLicencePlate(licencePlate);

        if (vehicleOpt.isPresent()) {
            Vehicle vehicle = vehicleOpt.get();
            boolean isParked = parkRepository.findByVehicleAndExitDateIsNull(vehicle).isPresent();
            vehicle.setParked(isParked);
        }
        return vehicleOpt;
    }
}
