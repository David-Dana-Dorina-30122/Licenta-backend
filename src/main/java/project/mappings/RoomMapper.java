package project.mappings;

import project.dto.RoomDTO;
import project.room.Room;

public class RoomMapper {
    public static RoomDTO toDto(Room room) {
        RoomDTO dto = new RoomDTO();
        dto.setId(room.getId());
        dto.setNumber(room.getNumber());
        dto.setType(room.getType().name());
        dto.setDescription(room.getDescription());
        dto.setPricePerNight(room.getPricePerNight());
        dto.setStatus(room.getStatus().name());
        dto.setCapacity(room.getCapacity());
        return dto;
    }
}

