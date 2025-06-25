package project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import project.restaurant.Restaurant;

@SpringBootTest
@AutoConfigureMockMvc
public class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createRestaurantTest() throws Exception {
        var restaurant = new Restaurant();
        restaurant.setFoodName("Pizza");
        restaurant.setDescription("Pizza cu ciuperci");
        restaurant.setPhotoURL("linkPoza");
//        restaurant.setPrice(20.0);
        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/addFood")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurant))
                        )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    public void deleteRestaurantTest() throws Exception {
        var restaurant = new Restaurant();
        int id = 18;

        mockMvc.perform(MockMvcRequestBuilders.delete("/restaurants/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurant))
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
