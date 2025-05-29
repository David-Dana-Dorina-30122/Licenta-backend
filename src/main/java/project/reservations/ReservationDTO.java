//package project.reservations;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.Data;
//
//import java.util.Date;
//
//@Data
//public class ReservationDTO {
//    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // hides field 'id' for create in swagger
//    private Integer id;
//
//    private String idNumber;
//
//    private Date dataCheckIn;
//
//    private Date dataCheckOut;
//
//    private ReservationStatus status;
//
//    private Double totalCost;
//}
