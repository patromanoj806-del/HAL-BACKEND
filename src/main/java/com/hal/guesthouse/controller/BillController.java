package com.hal.guesthouse.controller;

import com.hal.guesthouse.model.Bill;
import com.hal.guesthouse.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/bills")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BillController {

    private final BillService billService;

    // POST /api/bills/generate/{bookingId}  -- Generate bill on checkout
    @PostMapping("/generate/{bookingId}")
    public ResponseEntity<Bill> generateBill(@PathVariable String bookingId) {
        return ResponseEntity.ok(billService.generateBill(bookingId));
    }

    // GET /api/bills/booking/{bookingId}  -- Get bill by booking ID
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<Bill> getBillByBooking(@PathVariable String bookingId) {
        return ResponseEntity.ok(billService.getBillByBookingId(bookingId));
    }

    // GET /api/bills/{billNumber}  -- Get bill by bill number
    @GetMapping("/{billNumber}")
    public ResponseEntity<Bill> getBillByNumber(@PathVariable String billNumber) {
        return ResponseEntity.ok(billService.getBillByBillNumber(billNumber));
    }

    // PUT /api/bills/{billNumber}/pay  -- Mark bill as paid
    @PutMapping("/{billNumber}/pay")
    public ResponseEntity<?> markAsPaid(@PathVariable String billNumber) {
        Bill bill = billService.markAsPaid(billNumber);
        return ResponseEntity.ok(Map.of("result", "PAYMENT_SUCCESS", "billNumber", bill.getBillNumber()));
    }
}
