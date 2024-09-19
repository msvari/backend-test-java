package com.fcamara.park.repository;

import com.fcamara.park.model.Vehicle;
import com.fcamara.park.model.enumeration.Color;
import com.fcamara.park.model.enumeration.VehicleBrand;
import com.fcamara.park.model.enumeration.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

    Optional<Vehicle> findByLicencePlate(String licencePlate);

    List<Vehicle> findByBrandOrModelOrColorOrType(VehicleBrand brand, String model, Color color, VehicleType vehicleType);

}
