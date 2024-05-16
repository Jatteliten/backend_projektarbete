package com.example.backend.repos;

import com.example.backend.model.Booking;
import com.example.backend.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepo extends JpaRepository<Booking, Long> {
    List<Booking> findByCustomer_Id(Long customerId);
//    List<Booking> findByCustomer_IdAndstartDateAfter(Long id, LocalDate dateOneYearAgo);
    List<Booking> findByCustomerIdAndStartDateWithinOneYear(Long customerId, LocalDate startDate);
}
