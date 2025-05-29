package project.dto;

import lombok.Data;

@Data
public class ReviewDTO {
    private int rating;
    private String comment;
    private int reservationId;
}
