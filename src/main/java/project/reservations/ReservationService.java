package project.reservations;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dto.ExternalReservationDTO;
import project.dto.ReservationDTO;
import project.room.Room;
import project.room.RoomRepository;
import project.security.EmailService;
import project.user.User;
import project.user.UserRepository;
import project.user.UserService;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final EmailService emailService;
    private final UserService userService;

    public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository, RoomRepository roomRepository, EmailService emailService, UserService userService) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.emailService = emailService;
        this.userService = userService;
    }

    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public Reservation createReservationForUser(ReservationDTO dto, String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

//        Room room = roomRepository.findById(dto.getRoomId())
//                .orElseThrow(() -> new RuntimeException("Room not found"));



        Room room = roomRepository.findByIdWithLock(dto.getRoomId());
        List<Reservation> conflicts = reservationRepository.findConflictingReservations(
                room.getId(), dto.getDataCheckIn(), dto.getDataCheckOut());

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Room is already booked for the selected dates");
        }

        Reservation reservation = new Reservation();
        reservation.setDataCheckIn(dto.getDataCheckIn());
        reservation.setDataCheckOut(dto.getDataCheckOut());
        reservation.setNumberOfPeople(dto.getNumberOfPeople());
        reservation.setNumberOfAdults(dto.getNumberOfAdults());
        reservation.setNumberOfChildren(dto.getNumberOfChildren());
        reservation.setTotalCost(dto.getTotalCost());
        reservation.setRoom(room);
        reservation.setUser(user);
        reservation.setStatus(ReservationStatus.ACTIVE);
        reservation.setCheckedInToken(UUID.randomUUID().toString());
        room = roomRepository.save(room);
//        reservation.setRoom(room);


        try {
            String checkInUrl = "https://licenta-backend-production-d411.up.railway.app/reservations/checkin/" + reservation.getCheckedInToken();
            byte[] qrImage = generateQRCodeImage(checkInUrl, 300, 300);

            emailService.sendQRCodeEmail(reservation.getUser().getEmail(), qrImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reservationRepository.save(reservation);

    }

    public List<Reservation> getReservationsForUsername(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilizatorul nu a fost găsit"));

        return reservationRepository.findByUser(user);
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation getById(int id){
        return reservationRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Rezervarea nu a fost găsită"));
    }

    public Reservation update(int id, Reservation newReservation) {
            Reservation existingReservation = reservationRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Rezervarea nu a fost gasita: " + id));

            if(newReservation.getDataCheckIn() != null){
                existingReservation.setDataCheckIn(newReservation.getDataCheckIn());
            }
            if(newReservation.getDataCheckOut() != null){
                existingReservation.setDataCheckOut(newReservation.getDataCheckOut());
            }
        if (newReservation.getCheckedInAt() != null) {
            existingReservation.setCheckedInAt(newReservation.getCheckedInAt());
        }
        if (newReservation.getCheckedOutAt() != null) {
            existingReservation.setCheckedOutAt(newReservation.getCheckedOutAt());
        }

        if(newReservation.getStatus() != null){
            existingReservation.setStatus(newReservation.getStatus());
            }
             if(newReservation.getTotalCost() != null){
                existingReservation.setTotalCost(newReservation.getTotalCost());
            }

        return reservationRepository.save(existingReservation);
    }

    @Transactional
    public void delete(int id) {
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rezervarea nu a fost gasita"));

        System.out.println("Deleting reservation: " + existingReservation);

        if (existingReservation.getUser() != null) {
            existingReservation.getUser().getReservations().remove(existingReservation);
        }

        if (existingReservation.getRoom() != null) {
            existingReservation.getRoom().getReservations().remove(existingReservation);
        }

        reservationRepository.delete(existingReservation);
        reservationRepository.flush();
    }

    public void createExternalReservation(ExternalReservationDTO dto) {
        Reservation reservation = new Reservation();
        reservation.setDataCheckIn(dto.getCheckIn());
        reservation.setDataCheckOut(dto.getCheckOut());
        reservation.setNumberOfAdults(dto.getAdults());
        reservation.setNumberOfChildren(dto.getChildren());
        reservation.setNumberOfPeople(dto.getAdults() + dto.getChildren());
        reservation.setStatus(ReservationStatus.ACTIVE);

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new RuntimeException("Camera nu a fost gasita"));
        reservation.setRoom(room);

        User externalUser = userService.findOrCreateExternalUser(dto.getGuestEmail());
        reservation.setUser(externalUser);

        reservationRepository.save(reservation);

        try {
            String checkInUrl = "https://licenta-backend-production-d411.up.railway.app/reservations/checkin/" + reservation.getCheckedInToken();
            byte[] qrImage = generateQRCodeImage(checkInUrl, 300, 300);

            emailService.sendQRCodeEmail(reservation.getUser().getEmail(), qrImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public byte[] generateQRCodeImage(String text, int width, int height) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

    @Scheduled(fixedRate = 360000)
    @Transactional
    public void markReservationsAsInactive() {
        Date today = new Date();
        List<Reservation> activeReservations = reservationRepository.findByStatus(ReservationStatus.ACTIVE);
        for (Reservation res : activeReservations) {
            if (res.getDataCheckOut().before(today) || res.getCheckedOutAt()!=null) {
                res.setStatus(ReservationStatus.COMPLETED);
                reservationRepository.save(res);
            }
        }
    }

}