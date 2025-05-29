package project.dto;


import lombok.Data;

@Data
public class RoomDTO {
    private int id;
    private int number;
    private String type;
    private String description;
    private double pricePerNight;
    private String status;
    private int capacity;
}
