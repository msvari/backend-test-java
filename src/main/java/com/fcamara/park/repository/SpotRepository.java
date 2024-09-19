package com.fcamara.park.repository;

import com.fcamara.park.model.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpotRepository extends JpaRepository<Spot, UUID> {

    Optional<Spot> findByCnpj(String cnpj);

    List<Spot> findByNameContaining(String name);

}
