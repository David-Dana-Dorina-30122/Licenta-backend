package project.reservations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.user.User;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {


    @Query("SELECT r FROM Reservation r WHERE r.room.id = :roomId " +
            "AND ((DATE(r.dataCheckIn) <= :checkOut AND DATE(r.dataCheckOut) >= :checkIn))")
    List<Reservation> findConflictingReservations(@Param("roomId") int roomId,
                                                  @Param("checkIn") Date checkIn,
                                                  @Param("checkOut") Date checkOut);

    List<Reservation> findByUser(User user);

    Optional<Reservation> findByCheckedInToken(String token);

    List<Reservation> findByStatus(ReservationStatus reservationStatus);
}
