package project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ExternalRoomDTO {
    private int id;
    private Integer number;
    private double pricePerNight;
    private Integer capacity;
    private List<String> amenities;
}

