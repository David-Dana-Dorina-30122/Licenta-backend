package project.reservations;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import io.jsonwebtoken.impl.security.EdwardsCurve;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dto.ReservationDTO;
import project.room.Availability;
import project.room.Room;
import project.room.RoomRepository;
import project.user.User;
import project.user.UserRepository;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service // creates one instance of this class
@Slf4j // adds the 'log(ger)' instance to this class

@Transactional
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;


    public Reservation createReservationForUser(ReservationDTO dto, String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

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

        room.setStatus(Availability.UNAVAILABLE);
        room = roomRepository.save(room);
        reservation.setRoom(room);

        return reservationRepository.save(reservation);
    }

    public List<Reservation> findConflictingReservations(int roomId, Date startDate, Date endDate) {
        return reservationRepository.findByRoomIdAndDataCheckInBetweenOrDataCheckOutBetween(roomId, startDate, endDate, startDate, endDate);
    }

    public List<Reservation> getReservationsForUsername(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return reservationRepository.findByUser(user);
    }

//    public Reservation read(int idNumber) {
//        return reservationRepository.findById(idNumber).orElseThrow(() -> new RuntimeException("Reservation not found"));
//    }
//
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation update(int id, Reservation newReservation) {
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));

        existingReservation.setDataCheckIn(newReservation.getDataCheckIn());
        existingReservation.setDataCheckOut(newReservation.getDataCheckOut());
        existingReservation.setStatus(newReservation.getStatus());
        existingReservation.setTotalCost(newReservation.getTotalCost());

        return reservationRepository.save(existingReservation);
    }

    @Transactional
    public void delete(int id) {
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        System.out.println("Deleting reservation: " + existingReservation);

        // IMPORTANT: Elimină manual din relațiile bidirecționale
        if (existingReservation.getUser() != null) {
            existingReservation.getUser().getReservations().remove(existingReservation);
        }

        if (existingReservation.getRoom() != null) {
            existingReservation.getRoom().getReservations().remove(existingReservation);
        }

        reservationRepository.delete(existingReservation);
        reservationRepository.flush(); // forțează delete
    }


    public byte[] generateQRCodeImage(String text, int width, int height) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

    @Scheduled(fixedRate = 6000)
    @Transactional
    public void markReservationsAsInactive() {
        Date today = new Date();

        List<Reservation> activeReservations = reservationRepository.findByStatus(ReservationStatus.ACTIVE);

        for (Reservation res : activeReservations) {
            if (res.getDataCheckOut().before(today)) {
                res.setStatus(ReservationStatus.COMPLETED);
                reservationRepository.save(res);
            }
        }
    }

}