package project.payment;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.reservations.Reservation;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }


    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(int id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plata nu a fost găsită: " + id));
    }

    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public void deletePayment(int id) {
        if (!paymentRepository.existsById(id)) {
            throw new EntityNotFoundException("Plata nu a fost găsită: " + id);
        }
        paymentRepository.deleteById(id);
    }
}

