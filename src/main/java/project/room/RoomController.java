package project.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.dto.RoomDTO;
import project.mappings.RoomMapper;
import project.reservations.Reservation;
import project.reservations.ReservationRepository;
import project.reservations.ReservationService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/rooms")

public class RoomController {

    private final RoomService roomService;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public RoomController(RoomService roomService, RoomRepository roomRepository, ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.roomService = roomService;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/room/{roomId}/availability")
    public List<Reservation> getRoomAvailability(@PathVariable int roomId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return reservationRepository.findConflictingReservations(roomId, startDate, endDate);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Room>> getAvailableRooms(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @RequestParam int numberOfPeople,
            @RequestParam Type type) {

        List<Room> availableRooms = roomService.findAvailableRooms(checkIn, checkOut, numberOfPeople,type);
        return ResponseEntity.ok(availableRooms);
    }

    @PostMapping("/{id}/add-images")
    public ResponseEntity<Room> addImageUrls(@PathVariable int id, @RequestBody List<String> imageUrls) {
        Room updatedRoom = roomService.addImageUrls(id, imageUrls);
        return ResponseEntity.ok(updatedRoom);
    }



    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/{id}")
    public Room getRoomById(@PathVariable int id) {
        return roomService.getRoomById(id);
    }

    @PostMapping("/create")
    public RoomDTO createRoom(@RequestBody Room room) {
        Room saved = roomService.createRoom(room);
        return RoomMapper.toDto(saved);
    }


//    @PostMapping
//    public Room createRoom(@RequestBody Room room) {
//        return roomService.createRoom(room);
//    }

    @PutMapping("/{id}")
    public Room updateRoom(@PathVariable int id, @RequestBody Room room) {
        return roomService.updateRoom(id, room);
    }

    @DeleteMapping("/{id}")
    public void deleteRoom(@PathVariable int id) {
        roomService.deleteRoom(id);
    }
}


