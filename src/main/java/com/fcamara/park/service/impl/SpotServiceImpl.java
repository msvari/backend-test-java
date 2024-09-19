package com.fcamara.park.service.impl;

import com.fcamara.park.model.Spot;
import com.fcamara.park.repository.SpotRepository;
import com.fcamara.park.service.SpotService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SpotServiceImpl implements SpotService {

    SpotRepository spotRepository;

    @Override
    public Spot save(Spot spot) {
        return spotRepository.save(spot);
    }

    @Override
    public Optional<Spot> findById(UUID id) {
        return spotRepository.findById(id);
    }

    @Override
    public Optional<Spot> findByCnpj(String cnpj) {
        return spotRepository.findByCnpj(cnpj);
    }

    @Override
    public void delete(Spot spot) {
        spotRepository.delete(spot);
    }

    @Override
    public List<Spot> findSpot(String name) {
        return spotRepository.findByNameContaining(name);
    }
}
