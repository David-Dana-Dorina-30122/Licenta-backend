package project.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.reservations.Reservation;
import project.reservations.ReservationRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

//    public List<Room> findAvailableRooms(LocalDate dataCheckIn, LocalDate dataCheckOut, int numberOfPeople) {
//        List<Room> allRooms = roomRepository.findByCapacityGreaterThanEqual(numberOfPeople);
//
//        return allRooms.stream()
//                .filter(room -> reservationRepository
//                        .findConflictingReservations(room.getId(), java.sql.Date.valueOf(dataCheckIn), java.sql.Date.valueOf(dataCheckOut))
//                        .isEmpty())
//                .collect(Collectors.toList());
//    }

    public List<Room> findAvailableRooms(LocalDate dataCheckIn, LocalDate dataCheckOut, int numberOfPeople, Type type) {
        List<Room> allRooms = roomRepository.findByCapacityGreaterThanEqualAndType(numberOfPeople, type);
        System.out.println("camere" + allRooms);
        return allRooms.stream()
                .filter(room -> reservationRepository
                        .findConflictingReservations(
                                room.getId(),
                                java.sql.Date.valueOf(dataCheckIn),
                                java.sql.Date.valueOf(dataCheckOut)
                        )
                        .isEmpty()
                )
                .collect(Collectors.toList());
    }


    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(int id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
    }

    @Transactional
    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    public Room updateRoom(int id, Room updatedRoom) {
        Room existingRoom = getRoomById(id);
        existingRoom.setNumber(updatedRoom.getNumber());
        existingRoom.setType(updatedRoom.getType());
        existingRoom.setDescription(updatedRoom.getDescription());
        existingRoom.setPricePerNight(updatedRoom.getPricePerNight());
        existingRoom.setStatus(updatedRoom.getStatus());
        existingRoom.setCapacity(updatedRoom.getCapacity());
        return roomRepository.save(existingRoom);
    }

    public void deleteRoom(int id) {
        Room room = getRoomById(id);
        roomRepository.delete(room);
    }

    public Room addImageUrls(int roomId, List<String> newUrls) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (room.getImageUrls() == null) {
            room.setImageUrls(new ArrayList<>());
        }

        room.getImageUrls().addAll(newUrls);
        return roomRepository.save(room);
    }
}

