package com.example.backend.repos;

import com.example.backend.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepo extends JpaRepository<Booking, Long> {
    List<Booking> findByCustomer_Id(Long customerId);
}
