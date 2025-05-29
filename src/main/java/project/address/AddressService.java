package project.address;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.user.User;
import project.user.UserRepository;


import java.util.ArrayList;
import java.util.List;

@Service

public class AddressService {

    @Autowired
    private final AddressRepository addressRepository;
    @Autowired
    private  UserRepository userRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address create(Address address) {
        return addressRepository.save(address);
    }

    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    public Address getAddressById(int id) {
        return addressRepository.findById(id).orElseThrow(() -> new RuntimeException("Address not found"));
    }

    public Address updateAddress(int id, Address address) {
        Address existingAddress = addressRepository.findById(id).orElseThrow(() -> new RuntimeException("Address not found"));

        existingAddress.setCity(address.getCity());
        existingAddress.setCountry(address.getCountry());
        existingAddress.setStreet(address.getStreet());

        return addressRepository.save(existingAddress);
    }

    public void deleteAddress(int id) {
        Address existingAddress = addressRepository.findById(id).orElseThrow(() -> new RuntimeException("Address not found"));

        addressRepository.delete(existingAddress);
    }

    public List<Address> getAddressForUser(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return addressRepository.findAllByUser(user);
    }
}
