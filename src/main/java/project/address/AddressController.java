package project.address;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import project.reservations.Reservation;
import project.security.JwtService;
import project.user.User;
import project.user.UserRepository;
import project.user.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/addresses")
@CrossOrigin(origins = "http://localhost:4200")
public class AddressController {


    @Autowired
    public AddressService addressService;
    @Autowired
    public UserRepository userRepository;
    @Autowired
    private  AddressRepository addressRepository;
    @Autowired
    private JwtService jwtService;


    @GetMapping
    public List<Address> getAllAddress() {
        return addressService.getAllAddresses();
    }

    @GetMapping("/user-id-by-email")
    public ResponseEntity<Integer> getUserId(@RequestParam String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(u -> ResponseEntity.ok(u.getId()))
                .orElse(ResponseEntity.notFound().build());
    }

//    @GetMapping("/me")
//    public ResponseEntity<Address> getAddressForUser(@AuthenticationPrincipal UserDetails user) {
//        Address address = addressRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Address not found"));
//        return ResponseEntity.ok(address);
//    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyAddress(Principal principal) {
        String email = principal.getName();
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // sau throw
        }

        User user = userOpt.get();
        List<Address> address = user.getAddresses();

        if (address == null) {
            return ResponseEntity.ok().body(null); // sau new Address()
        }

        return ResponseEntity.ok(address);
    }


    @GetMapping("/my")
    public List<Address> getMyAddresses(@RequestHeader("Authorization") String authHeader) {
        String jwt = authHeader.substring(7); // remove "Bearer "
        String email = jwtService.extractUsername(jwt);
        User user = userRepository.findByEmail(email).orElseThrow();

        return addressRepository.findAllByUser(user);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addAddress(
            @RequestBody Address address,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(email).orElseThrow();

        address.setUser(user);
        return ResponseEntity.ok(addressRepository.save(address));
    }


    @GetMapping("/{id}")
    public Address getAddressById(@PathVariable int id) {
        return addressService.getAddressById(id);
    }

    @PostMapping
    public Address addAddress(@RequestBody Address address) {
        return addressService.create(address);
    }

    @PutMapping("/{id}")
    public Address updateAddress(@PathVariable int id, @RequestBody Address address) {
        return addressService.updateAddress(id, address);
    }

    @PutMapping("/update")
    public ResponseEntity<Address> updateAddress(@RequestBody Address updatedAddress, @AuthenticationPrincipal UserDetails user) {
        System.out.println("Received address: " + updatedAddress); // Log pentru verificare
        Address address = addressRepository.findByUser(user).orElseThrow();
        address.setStreet(updatedAddress.getStreet());
        address.setCity(updatedAddress.getCity());
        address.setCountry(updatedAddress.getCountry());
        return ResponseEntity.ok(addressRepository.save(address));
    }




//    @PutMapping("/update")
//    public ResponseEntity<Address> updateAddress(@RequestBody Address updatedAddress, @RequestHeader("Authorization") String authHeader) {
//        String token = authHeader.substring(7);
//        String email = jwtService.extractUsername(token);
//        User user = userRepository.findByEmail(email).orElseThrow();
//
//        Address address = addressRepository.findByUser(user).orElseThrow();
//        address.setStreet(updatedAddress.getStreet());
//        address.setCity(updatedAddress.getCity());
//        address.setCountry(updatedAddress.getCountry());
//
//        return ResponseEntity.ok(addressRepository.save(address));
//    }


    @DeleteMapping("/{id}")
    public void deleteAddress(@PathVariable int id) {
        addressService.deleteAddress(id);
    }

}
