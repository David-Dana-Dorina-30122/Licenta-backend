package project.restaurant;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dto.RestaurantDTO;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

//    @PostMapping("/addFood")
//    public ResponseEntity<Restaurant> addFood(@RequestBody RestaurantDTO dto) {
//        System.out.println("DTO: " + dto);
//        Restaurant restaurant = new Restaurant();
//        restaurant.setFoodName(dto.getFoodName());
//        restaurant.setPhotoURL(dto.getPhotoURL());
//        restaurant.setDescription(dto.getDescription());
//
//        Restaurant createdFood = restaurantService.addFood(restaurant);
//        return ResponseEntity.ok(createdFood);
//    }

    @PostMapping("/addFood")
    public ResponseEntity<Restaurant> createRestaurant(@Valid @RequestBody Restaurant restaurant) {
        Restaurant saved = restaurantService.addFood(restaurant);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/")
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        List<Restaurant> foods = restaurantService.getAllFoods();
        return ResponseEntity.ok(foods);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable int id, @RequestBody Restaurant restaurant) {
         restaurantService.updateFood(id,restaurant);
         return ResponseEntity.ok(restaurant);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Restaurant> deleteRestaurant(@PathVariable int id) {
         restaurantService.deleteFood(id);
         return ResponseEntity.ok().build();
    }
}
