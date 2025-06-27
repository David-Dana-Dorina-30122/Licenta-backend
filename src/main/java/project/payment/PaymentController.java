package project.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dto.ReservationDTO;
import project.reservations.Reservation;
import project.reservations.ReservationService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/payments")
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentController {

    private final PaymentService paymentService;
    private final ReservationService reservationService;

    @Autowired
    public PaymentController(PaymentService paymentService, ReservationService reservationService) {
        this.paymentService = paymentService;
        this.reservationService = reservationService;
    }

    @PostMapping("/pay/{reservationId}")
    public ResponseEntity<Payment> pay(
            @PathVariable int reservationId,
            @RequestParam String method) {

        Reservation reservation = reservationService.getById(reservationId);

        Payment payment = new Payment();
        payment.setMethod(method);
        payment.setStatus("COMPLETED");
        payment.setPaymentDate(LocalDateTime.now());

        paymentService.createPayment(payment);

        reservation.setPayment(payment);
        reservationService.save(reservation);

        return ResponseEntity.ok(payment);
    }


    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    public Payment getPaymentById(@PathVariable int id) {
        return paymentService.getPaymentById(id);
    }

    @PostMapping
    public Payment createPayment(@RequestBody Payment payment) {
        return paymentService.createPayment(payment);

    }

    @DeleteMapping("/{id}")
    public void deletePayment(@PathVariable int id) {
        paymentService.deletePayment(id);
    }
}

