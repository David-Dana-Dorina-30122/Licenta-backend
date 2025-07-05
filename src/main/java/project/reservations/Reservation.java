package project.reservations;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import project.payment.Payment;
import project.review.Review;
import project.room.Room;
import project.user.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(updatable = false, nullable = false)
    private Date dataCheckIn;

    @Column(nullable = false)
    private Date dataCheckOut;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @Column
    private Double totalCost;

    @Column
    private int numberOfAdults;
    @Column
    private int numberOfChildren;

    @Column
    private int numberOfPeople;

    @Column
    private String checkedInToken;
    @Column
    private LocalDateTime checkedInAt;
    @Column
    private LocalDateTime checkedOutAt;
    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
//    @JsonIgnore
//    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    @ToString.Exclude
//    @JsonIgnore
   // @JsonBackReference
    private Room room;

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "payment_id")
    @JsonIgnore
    private Payment payment;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Review> reviews = new ArrayList<>();
}
