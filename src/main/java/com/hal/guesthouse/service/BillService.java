package com.hal.guesthouse.service;

import com.hal.guesthouse.model.*;
import com.hal.guesthouse.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class BillService {

    private final BillRepository billRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    public Bill generateBill(String bookingId) {
        Booking booking = bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));

        // Check if bill already exists
        if (billRepository.findByBooking_BookingId(bookingId).isPresent()) {
            return billRepository.findByBooking_BookingId(bookingId).get();
        }

        Room room = booking.getRoom();
        if (room == null) {
            throw new RuntimeException("No room assigned to this booking");
        }

        long days = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
        if (days <= 0) days = 1;

        double ratePerDay = room.getRatePerDay();
        double roomAmount = days * ratePerDay;
        double gstPercent = 18.0;
        double gstAmount = roomAmount * gstPercent / 100;
        double grandTotal = roomAmount + gstAmount;

        String billNumber = generateBillNumber();

        Bill bill = Bill.builder()
                .billNumber(billNumber)
                .booking(booking)
                .totalDays((int) days)
                .ratePerDay(ratePerDay)
                .roomAmount(roomAmount)
                .gstPercent(gstPercent)
                .gstAmount(gstAmount)
                .grandTotal(grandTotal)
                .billDate(LocalDate.now())
                .paymentStatus(Bill.PaymentStatus.PENDING)
                .build();

        return billRepository.save(bill);
    }

    public Bill getBillByBookingId(String bookingId) {
        return billRepository.findByBooking_BookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Bill not found for booking: " + bookingId));
    }

    public Bill getBillByBillNumber(String billNumber) {
        return billRepository.findByBillNumber(billNumber)
                .orElseThrow(() -> new RuntimeException("Bill not found: " + billNumber));
    }

    @Transactional
    public Bill markAsPaid(String billNumber) {
        Bill bill = getBillByBillNumber(billNumber);
        bill.setPaymentStatus(Bill.PaymentStatus.PAID);
        return billRepository.save(bill);
    }

    private String generateBillNumber() {
        long count = billRepository.count() + 1;
        return String.format("BILL%03d", count);
    }
}
