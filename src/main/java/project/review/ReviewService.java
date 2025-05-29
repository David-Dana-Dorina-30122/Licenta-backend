package project.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.reservations.Reservation;
import project.reservations.ReservationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public Review addReview(int reservationId, int rating, String comment) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Rezervare inexistentă"));

        Review review = new Review();
        review.setRating(rating);
        review.setComment(comment);
        review.setCreatedAt(LocalDateTime.now());
        review.setReservation(reservation);

        return reviewRepository.save(review);
    }

    public List<Review> getReviewsByReservationId(int reservationId) {
        return reviewRepository.findByReservationId(reservationId);
    }
}
