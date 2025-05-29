package project.room;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Integer> {

    List<Room> findByCapacityGreaterThanEqual(int capacity);
    List<Room> findByCapacityGreaterThanEqualAndType(int capacity, Type type);
}
