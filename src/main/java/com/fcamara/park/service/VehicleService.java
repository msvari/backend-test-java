package com.fcamara.park.service;

import com.fcamara.park.model.Vehicle;
import com.fcamara.park.model.enumeration.Color;
import com.fcamara.park.model.enumeration.VehicleBrand;
import com.fcamara.park.model.enumeration.VehicleType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VehicleService {

    Vehicle save(Vehicle vehicle);

    Optional<Vehicle> findById(UUID id);

    void delete(Vehicle vehicle);

    List<Vehicle> findVehicles(VehicleBrand brand, String model, Color color, VehicleType type);

    Optional<Vehicle> findByLicencePlate(String licencePlate);

}
