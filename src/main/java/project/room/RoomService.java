package project.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dto.ExternalRoomDTO;
import project.reservations.ReservationRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository, ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Room> findAvailableRooms(LocalDate dataCheckIn, LocalDate dataCheckOut, int numberOfPeople) {
        List<Room> allRooms = roomRepository.findByCapacityGreaterThanEqual(numberOfPeople);
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
                .orElseThrow(() -> new RuntimeException("Camera nu a fost găsită: " + id));
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
                .orElseThrow(() -> new RuntimeException("Camera nu a fost găsită"));

        if (room.getImageUrls() == null) {
            room.setImageUrls(new ArrayList<>());
        }

        room.getImageUrls().addAll(newUrls);
        return roomRepository.save(room);
    }

    public List<ExternalRoomDTO> getRoomsForExternal(LocalDate checkIn, LocalDate checkOut, int numberOfPeople) {
        List<Room> availableRooms = findAvailableRooms(checkIn, checkOut, numberOfPeople);
        return availableRooms.stream()
                .map(room -> new ExternalRoomDTO(
                        room.getId(),
                        room.getNumber(),
                        room.getPricePerNight(),
                        room.getCapacity(),
                        List.of("WiFi", "AC", "TV")
                ))
                .collect(Collectors.toList());
    }

}

