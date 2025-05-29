package project.reservations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.user.User;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @Query("""
    SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
    FROM Reservation r
    WHERE r.room.id = :roomId
      AND (
            (:checkIn BETWEEN r.dataCheckIn AND r.dataCheckOut)
         OR (:checkOut BETWEEN r.dataCheckIn AND r.dataCheckOut)
         OR (r.dataCheckIn BETWEEN :checkIn AND :checkOut)
         OR (r.dataCheckOut BETWEEN :checkIn AND :checkOut)
      )
""")
    boolean existsByRoomIdAndDateOverlap(@Param("roomId") int roomId,
                                         @Param("checkIn") Date checkIn,
                                         @Param("checkOut") Date checkOut);


    @Query("SELECT r FROM Reservation r WHERE r.room.id = :roomId " +
            "AND ((r.dataCheckIn <= :checkOut AND r.dataCheckOut >= :checkIn))")
    List<Reservation> findConflictingReservations(@Param("roomId") int roomId,
                                                  @Param("checkIn") Date checkIn,
                                                  @Param("checkOut") Date checkOut);

    List<Reservation> findByUser(User user);

    List<Reservation> findByRoomIdAndDataCheckInBetweenOrDataCheckOutBetween(int room_id, Date dataCheckIn, Date dataCheckIn2, Date dataCheckOut, Date dataCheckOut2);

    @Modifying
    @Query("DELETE FROM Reservation r WHERE r.user.id = :userId")
    void deleteByUserId(@Param("userId") int userId);

    Optional<Reservation> findByCheckedInToken(String token);

    List<Reservation> findByStatus(ReservationStatus reservationStatus);
}
