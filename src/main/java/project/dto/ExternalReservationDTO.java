package project.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ExternalReservationDTO {
    private String guestEmail;
    private Date checkIn;
    private Date checkOut;
    private int adults;
    private int children;
    private int roomId;
}

