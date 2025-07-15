package project.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.user.Role;
import project.user.User;
import project.user.UserRepository;

@Configuration
public class AdminInitializer {

    @Value("${adminEmail}")
    private String adminEmail;
    @Value("${adminPassword}")
    private String adminPassword;

        @Bean
        public CommandLineRunner initData(UserRepository userRepository,
                                          PasswordEncoder passwordEncoder) {
            return args -> {
                if (userRepository.findByEmail(adminEmail).isEmpty()) {

                    User admin = new User();
                    admin.setEmail(adminEmail);
                    admin.setPassword(passwordEncoder.encode(adminPassword));
                    admin.setEnabled(true);
                    admin.setRole(Role.ROLE_ADMIN);

                    userRepository.save(admin);

                    System.out.println("Admin creat.");
                }
            };
        }
}
