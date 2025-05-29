package project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import project.reservations.ReservationStatus;

import java.util.Date;


@Data
public class ReservationDTO {

    private Integer roomId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dataCheckIn;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dataCheckOut;

    private int numberOfPeople;

    private int numberOfAdults;

    private int numberOfChildren;

    private Double totalCost;

}
