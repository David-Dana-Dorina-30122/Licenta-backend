package project.config;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import project.dto.ExternalReservationDTO;
import project.dto.ExternalRoomDTO;
import project.reservations.ReservationService;
import org.springframework.http.ResponseEntity;
import project.room.RoomService;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/external")
@RequiredArgsConstructor
public class BookingReservationController {
    private final ReservationService reservationService;
    private final RoomService roomService;

    @PostMapping("/reservation")
    public ResponseEntity<String> receiveReservation(
            @RequestBody ExternalReservationDTO dto
    ) {
        reservationService.createExternalReservation(dto);
        return ResponseEntity.ok("Rezervare primită");
    }
    @GetMapping("/rooms")
    public ResponseEntity<?> getRoomsForExternal(
            @RequestParam("checkIn")  LocalDate checkIn,
            @RequestParam("checkOut") LocalDate checkOut,
            @RequestParam("numberOfPeople") int numberOfPeople
    ) {
        List<ExternalRoomDTO> rooms = roomService.getRoomsForExternal(checkIn, checkOut, numberOfPeople);
        if (rooms.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Camere indisponibile");
        } else {
            return ResponseEntity.ok(rooms);
        }
    }
}


