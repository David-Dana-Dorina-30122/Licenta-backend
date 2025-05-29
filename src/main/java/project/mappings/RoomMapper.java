package project.mappings;

import project.dto.RoomDTO;
import project.room.Availability;
import project.room.Room;
import project.room.Type;

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

    public static Room fromDto(RoomDTO dto) {
        Room room = new Room();
        room.setNumber(dto.getNumber());
        room.setType(Type.valueOf(dto.getType()));
        room.setDescription(dto.getDescription());
        room.setPricePerNight(dto.getPricePerNight());
        room.setStatus(Availability.valueOf(dto.getStatus()));
        room.setCapacity(dto.getCapacity());
        return room;
    }

}

