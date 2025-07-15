package project.payment;

import jakarta.persistence.*;
import lombok.Data;
import project.reservations.Reservation;
import java.time.LocalDateTime;

@Entity
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String method;
    private String status;
    private LocalDateTime paymentDate;

    @OneToOne
    private Reservation reservation;


}


