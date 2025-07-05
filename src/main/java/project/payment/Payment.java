package project.payment;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import project.reservations.Reservation;

import java.time.LocalDateTime;

@Entity
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String method; // ex: CARD, CASH, FAKE
    private String status; // PENDING, COMPLETED, FAILED
    private LocalDateTime paymentDate;

    @OneToOne
    private Reservation reservation;


}


