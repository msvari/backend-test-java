package com.fcamara.park.repository;

import com.fcamara.park.model.enumeration.VehicleType;
import com.fcamara.park.model.Park;
import com.fcamara.park.model.Vehicle;
import com.fcamara.park.model.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParkRepository extends JpaRepository<Park, UUID> {

    Optional<Park> findByVehicleAndExitDateIsNull(Vehicle vehicle);
    Optional<Park> findByVehicleAndSpotAndExitDateIsNull(Vehicle vehicle, Spot spot);
    List<Park> findAllBySpot(Spot Spot);
    List<Park> findAllBySpotAndExitDateIsNull(Spot spot);
    @Query("SELECT COUNT(p) FROM Park p WHERE p.spot = :spot AND p.exitDate IS NULL AND p.vehicle.type = :type")
    long countParkedVehiclesBySpotAndType(Spot spot, VehicleType type);
}
