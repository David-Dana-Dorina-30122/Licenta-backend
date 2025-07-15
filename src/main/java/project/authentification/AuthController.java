package project.authentification;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dto.LoginUserDto;
import project.dto.RegisterUserDto;
import project.dto.ResetPasswordDto;
import project.dto.VerifyUserDto;
import project.user.User;


import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authenticationService;

    public AuthController(AuthService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUserDto input) {
        String token = authenticationService.authenticate(input);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDto verifyUserDto) {
        try {
            authenticationService.verifyUser(verifyUserDto);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Cont verificat cu succes");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        System.out.println("Email primit pentru retrimitere: " + email);
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Emailul este necesar");
        }

        try {
            authenticationService.resendVerificationCode(email);
            return ResponseEntity.ok("Cod de verificare trimis");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        try {
            authenticationService.sendPasswordResetEmail(email);
            return ResponseEntity.ok(Map.of("message", "Link pentru parolă trimis."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }


    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDto resetDto) {
        try {
            authenticationService.resetPassword(resetDto);
            return ResponseEntity.ok(Map.of("message", "Parolă schimbată cu succes."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}