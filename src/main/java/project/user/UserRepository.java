package project.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    List<User> findByVerificationCodeExpiresAtBefore(LocalDateTime dateTime);

    @Modifying
    @Query("DELETE FROM User u WHERE u.verificationCodeExpiresAt < :now")

    Optional<User> findByResetToken(String token);
}
