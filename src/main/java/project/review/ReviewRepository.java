package project.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.dto.ReviewDTO;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByReservationId(int reservationId);

    @Query("""
    SELECT new project.dto.ReviewDTO(r.rating, r.comment, r.reservation.id, r.createdAt)
    FROM Review r
""")
    List<ReviewDTO> getAllReviews();
}

