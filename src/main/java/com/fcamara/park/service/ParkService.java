package com.fcamara.park.service;

import com.fcamara.park.model.Spot;
import com.fcamara.park.model.Park;
import com.fcamara.park.model.Vehicle;
import com.fcamara.park.model.enumeration.VehicleType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParkService {

    Park parkVehicle(Vehicle vehicle, UUID spotId);

    Park unparkVehicle(Vehicle vehicle, UUID spotId);

    List<Park> findAllBySpot(Spot spot);

    Optional<Park> findByVehicleAndExitDateIsNull(Vehicle vehicle);

    Optional<Park> findByVehicleAndSpotAndExitDateIsNull(Vehicle vehicle, Spot spot);

    Long availableLimit(Spot spot, VehicleType type);

}
