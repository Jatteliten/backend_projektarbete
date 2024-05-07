package com.example.backend.repos;

import com.example.backend.model.Shipper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipperRepo extends JpaRepository<Shipper,Long> {
    Shipper findByExternalId(int id);
}
