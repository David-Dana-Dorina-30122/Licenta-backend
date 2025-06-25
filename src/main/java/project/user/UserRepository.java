package project.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.enabled = false AND u.verificationCodeExpiresAt < :now")
    void deleteExpiredUnverifiedUsers(@Param("now") LocalDateTime now);

    List<User> findByVerificationCodeExpiresAtBefore(LocalDateTime dateTime);

    @Modifying
    @Query("DELETE FROM User u WHERE u.verificationCodeExpiresAt < :now")
    void deleteExpiredUsers(@Param("now") LocalDateTime now);

    Optional<User> findByResetToken(String token);
}
