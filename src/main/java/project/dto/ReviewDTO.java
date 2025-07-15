package project.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDTO {
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
    private int reservationId;

    public ReviewDTO(int rating, String comment, int reservationId, LocalDateTime createdAt) {
        this.rating = rating;
        this.comment = comment;
        this.reservationId = reservationId;
        this.createdAt = createdAt;
    }

}
