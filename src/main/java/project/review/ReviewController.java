package project.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import project.dto.ReviewDTO;
import project.reservations.Reservation;
import project.reservations.ReservationRepository;
import project.user.User;
import project.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    private final ReviewRepository reviewRepository;

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    public ReviewController(ReviewService reviewService, ReviewRepository reviewRepository, ReservationRepository reservationRepository, UserRepository userRepository) {
        this.reviewService = reviewService;
        this.reviewRepository = reviewRepository;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/{reservationId}")
    public ResponseEntity<Review> addReview(
            @PathVariable int reservationId,
            @RequestBody Map<String, String> payload) {

        int rating = Integer.parseInt(payload.get("rating"));
        String comment = payload.get("comment");

        Review savedReview = reviewService.addReview(reservationId, rating, comment);
        return ResponseEntity.ok(savedReview);
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<List<Review>> getReviews(@PathVariable int reservationId) {
        return ResponseEntity.ok(reviewService.getReviewsByReservationId(reservationId));
    }


    @PostMapping
    public ResponseEntity<?> addReview(@RequestBody ReviewDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        // Autentificare și validare utilizator
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Reservation reservation = reservationRepository.findById(dto.getReservationId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found"));

        if (reservation.getUser().getId() != user.getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Rezervarea nu îți aparține.");
        }


        Review review = new Review();
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        review.setCreatedAt(LocalDateTime.now());
        review.setReservation(reservation);

        Review savedReview = reviewRepository.save(review);
        return ResponseEntity.ok(savedReview);
    }

    @GetMapping("/by-reservation/{reservationId}")
    public ResponseEntity<List<Review>> getReviewsByReservation(@PathVariable int reservationId) {
        List<Review> reviews = reviewRepository.findByReservationId(reservationId);
        return ResponseEntity.ok(reviews);
    }

}

