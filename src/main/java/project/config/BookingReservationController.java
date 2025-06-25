package project.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import project.dto.ExternalReservationDTO;
import project.reservations.ReservationService;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/external")
@RequiredArgsConstructor
public class BookingReservationController {
    @Value("${booking.api.key}")
    private String secretApiKey;

    private final ReservationService reservationService;

    @PostMapping("/reservation")
    public ResponseEntity<?> receiveReservation(
            @RequestBody ExternalReservationDTO dto,
            @RequestHeader("X-API-KEY") String apiKey
    ) {

        if (!apiKey.equals(secretApiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        reservationService.createExternalReservation(dto);

        return ResponseEntity.ok("Reservation received");
    }
}


