package project.restaurant;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public List<Restaurant> getAllFoods() {
        return restaurantRepository.findAll();
    }

    public Restaurant getFoodById(int id) {
        return restaurantRepository.findById(id).orElseThrow(() -> new RuntimeException("Itemul nu a fost găsit" + id));
    }

    public Restaurant getFoodByName(String name) {
        return restaurantRepository.findByFoodName(name);
    }

    public Restaurant addFood(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public void updateFood(int id, Restaurant food) {
        Restaurant existingFood = restaurantRepository.getFoodById(id)
                .orElseThrow(() -> new RuntimeException("Itemul nu a fost găsit: " + id));

        if (food.getFoodName() != null) {
            existingFood.setFoodName(food.getFoodName());
        }
        if (food.getPhotoURL() != null) {
            existingFood.setPhotoURL(food.getPhotoURL());
        }
        if (food.getPrice() > 0 ) {
            existingFood.setPrice(food.getPrice());
        }
        if (food.getDescription() != null) {
            existingFood.setDescription(food.getDescription());
        }
        restaurantRepository.save(existingFood);
    }

    public void  deleteFood(int id) {
        Restaurant existingFood = getFoodById(id);
        restaurantRepository.delete(existingFood);
    }
}
