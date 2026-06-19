package com.hal.guesthouse.repository;

import com.hal.guesthouse.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findByBooking_BookingId(String bookingId);
    Optional<Bill> findByBillNumber(String billNumber);
}
