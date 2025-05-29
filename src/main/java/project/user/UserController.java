package project.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import project.address.Address;
import project.dto.UserDto;
import project.security.JwtService;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;

    private final JwtService jwtService;
    @Autowired
    private UserRepository userRepository;

    public UserController(JwtService jwtService) {
        this.jwtService = jwtService;
    }


    @GetMapping("/role")
    public String getUserRole(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String role = jwtService.extractRole(token);
        return "Rolul utilizatorului este: " + role;
    }

//    @PostMapping
//    public User create(@RequestBody User user) {
//        return userService.create(user);
//    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(UserDto.fromUser(currentUser));
    }


    @GetMapping("/")
    public ResponseEntity<List<User>> allUsers() {
        List <User> users = userService.allUsers();
        return ResponseEntity.ok(users);
    }

//    @GetMapping
//    public List<User> getAllUsers (){
//        return userService.allUsers();
//    }

    @GetMapping("/{id}")
    public User read(@PathVariable int id){
        return userService.read(id);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable int id,@RequestBody User user){
        return userService.update(id,user);
    }
    @PutMapping("/me")
    public ResponseEntity<?> updateCurrentUser(@RequestBody User updatedUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        User updated = userService.update(currentUser.getId(), updatedUser);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/role/{id}")
    public User updateRole(@PathVariable int id,@RequestParam Role role) {
        return userService.updateRole(id,role);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable int id){
        userService.delete(id);
    }

    @DeleteMapping("/remove-unverified")
    public ResponseEntity<String> cleanExpiredUsers() {
        userService.cleanExpiredUsers();
        return ResponseEntity.ok("Utilizatorii neconfirmați au fost șterși.");
    }
}
