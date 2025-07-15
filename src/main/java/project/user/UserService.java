package project.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(User user) {
        return userRepository.save(user);
    }

    public User read(int id) {
      return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Utilizatorul nu a fost găsit:" + id));
    }

    public List<User> allUsers(){
        return userRepository.findAll();
    }

    public User update(int id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilizatorul nu a fost găsit:" + id));

        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        existingUser.setPhone(updatedUser.getPhone());
        return userRepository.save(existingUser);
    }

    public User updateRole(int id, Role updatedRole) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilizatorul nu a fost găsit:" + id));
        existingUser.setRole(updatedRole);

        return userRepository.save(existingUser);
    }

    public void delete(int id) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Utilizatorul nu a fost găsit:" + id));

        existingUser.getReservations().forEach(reservation -> reservation.setUser(null));
        existingUser.getReservations().clear();

        existingUser.getAddresses().forEach(address -> address.setUser(null));
        existingUser.getAddresses().clear();

        userRepository.deleteById(existingUser.getId());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilizatorul nu a fost găsit"));

        return new CustomUserDetails(user);
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cleanExpiredUsers() {
        LocalDateTime now = LocalDateTime.now();
        List<User> expiredUsers = userRepository.findByVerificationCodeExpiresAtBefore(now);
        for (User user : expiredUsers) {
            userRepository.deleteById(user.getId());
        }
    }

    public User findOrCreateExternalUser(String email) {
        return userRepository.findByEmail(email).orElseGet(() -> {
            User user = new User();
            user.setEmail(email);
            user.setPassword("external");
            user.setEnabled(true);
            return userRepository.save(user);
        });
    }

}

