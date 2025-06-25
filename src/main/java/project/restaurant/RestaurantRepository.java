package project.restaurant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.reservations.Reservation;

import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    public Restaurant findByFoodName(String name);
    Optional<Restaurant>  getFoodById(int id);
}
