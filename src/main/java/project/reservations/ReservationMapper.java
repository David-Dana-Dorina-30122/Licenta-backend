//package project.reservations;
//
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//
//@Data
//@Component // creates one instance of this class
//@Slf4j // adds the 'log(ger)' instance to this class
//public class ReservationMapper {
//    public ReservationDTO reservationToDto(Reservation reservation) {
//        ReservationDTO reservationDTO = new ReservationDTO();
//        reservationDTO.setId(reservation.getId());
//        reservationDTO.setIdNumber(reservation.getIdNumber());
//        reservationDTO.setDataCheckIn(reservation.getDataCheckIn());
//        reservationDTO.setDataCheckOut(reservation.getDataCheckOut());
//        reservationDTO.setStatus(reservation.getStatus());
//        reservationDTO.setTotalCost(reservation.getTotalCost());
//        return reservationDTO;
//    }
//    public Reservation dtoToReservation(ReservationDTO rDTO) {
//        Reservation reservation = new Reservation();
//        reservation.setId(rDTO.getId());
//        reservation.setIdNumber(rDTO.getIdNumber());
//        reservation.setDataCheckIn(rDTO.getDataCheckIn());
//        reservation.setDataCheckOut(rDTO.getDataCheckOut());
//        reservation.setStatus(rDTO.getStatus());
//        reservation.setTotalCost(rDTO.getTotalCost());
//        return reservation;
//    }
//}
