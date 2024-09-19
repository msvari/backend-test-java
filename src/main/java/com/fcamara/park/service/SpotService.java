package com.fcamara.park.service;

import com.fcamara.park.model.Spot;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpotService {
    Spot save(Spot vehicle);

    Optional<Spot> findById(UUID id);

    Optional<Spot> findByCnpj(String cnpj);

    void delete(Spot spot);

    List<Spot> findSpot(String name);
}
