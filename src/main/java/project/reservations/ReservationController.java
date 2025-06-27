package project.reservations;


import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import project.dto.ReservationDTO;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reservations")
@CrossOrigin(origins = "http://localhost:4200")
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationController(ReservationService reservationService, ReservationRepository reservationRepository) {
        this.reservationService = reservationService;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/my")
    public ResponseEntity<List<Reservation>> getReservationsForCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();
        System.out.println("Username from JWT: " + username);
        List<Reservation> reservations = reservationService.getReservationsForUsername(username);

        return ResponseEntity.ok(reservations);
    }

    @PostMapping("/create")
    public ResponseEntity<Reservation> createReservation (
            @RequestBody ReservationDTO reservationDto,
            @AuthenticationPrincipal UserDetails userDetails) throws MessagingException {

        String username = userDetails.getUsername();
        System.out.println("Username " + username);
        Reservation reservation = reservationService.createReservationForUser(reservationDto, username);
        System.out.println("Reservation: " + reservation);
        if (reservation != null) {
            return ResponseEntity.ok(reservation);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/findAll")
    public List<Reservation> findAll() {
        return reservationService.findAll();
    }

    @PutMapping("/{idNumber}")
    public Reservation update(@PathVariable int idNumber, @RequestBody Reservation reservation) {
        return reservationService.update(idNumber, reservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteReservation(@PathVariable int id) {
        reservationService.delete(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Rezervare ștearsă cu succes");
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}/qr")
    public ResponseEntity<byte[]> getReservationQRCode(@PathVariable int id) throws Exception {
        Reservation res = reservationRepository.findById(id).orElseThrow();
        String token = res.getCheckedInToken();
        String checkInUrl = "https://licenta-backend-production-d411.up.railway.app/reservations/check-in/" + token;

        System.out.println("token: " + token);

        byte[] qrImage = reservationService.generateQRCodeImage(checkInUrl, 300, 300);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(qrImage);
    }

    @GetMapping("/check-in/{token}")
    public ResponseEntity<String> checkIn(@PathVariable String token) {
        Reservation res = reservationRepository.findByCheckedInToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Token invalid"));

        if (res.getCheckedInAt() != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Deja s-a făcut check-in.");
        }

        res.setCheckedInAt(LocalDateTime.now());
        reservationRepository.save(res);

        String html = "<html><body>"
                + "<h1>Confirmă check-in pentru rezervarea #" + res.getId() + "</h1>"
                + "<form method='POST' action='/reservations/check-in'>"
                + "<input type='hidden' name='token' value='" + token + "'/>"
                + "<button type='submit'>Confirmă check-in</button>"
                + "</form></body></html>";

        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(html);
    }


}