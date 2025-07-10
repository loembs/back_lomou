package ism.atelier.atelier.data.repository;

import ism.atelier.atelier.data.models.Plate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlateRepository extends JpaRepository<Plate, Long> {
} 