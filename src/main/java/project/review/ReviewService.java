package project.review;

import org.springframework.stereotype.Service;
import project.dto.ReviewDTO;
import project.reservations.Reservation;
import project.reservations.ReservationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    private final ReservationRepository reservationRepository;

    private final ReviewRepository reviewRepository;

    public ReviewService(ReservationRepository reservationRepository, ReviewRepository reviewRepository) {
        this.reservationRepository = reservationRepository;
        this.reviewRepository = reviewRepository;
    }

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

    public List<ReviewDTO> getAllReviews() {
        return reviewRepository.getAllReviews();
    }
}
