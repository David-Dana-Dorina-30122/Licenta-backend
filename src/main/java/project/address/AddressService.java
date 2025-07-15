package project.address;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService( AddressRepository addressRepository) {
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
}
