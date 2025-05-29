//package project.qrCode;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RestController;
//import project.reservations.Reservation;
//import project.reservations.ReservationRepository;
//
//import java.util.UUID;
//
//@RestController
//public class qrCodeController {
//
//    @Autowired
//    private ReservationRepository reservationRepository;
//    @Autowired
//    private qrCodeService qrCodeService;
//
//
//    @GetMapping("/reservation/{id}/qr")
//    public ResponseEntity<byte[]> getReservationQRCode(@PathVariable int id) throws Exception {
//       // Reservation res = reservationRepository.findById(id).orElseThrow();
//        String token = UUID.randomUUID().toString();
//
//        byte[] qrImage = qrCodeService.generateQRCodeImage(token, 300, 300);
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.IMAGE_PNG)
//                .body(qrImage);
//    }
//
//}
