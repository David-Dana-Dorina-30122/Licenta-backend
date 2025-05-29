package project.room;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import project.reservations.Reservation;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int number;
    @Column(nullable = false)
    private Type type;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Double pricePerNight;
    @Column(nullable = false)
    private Availability status;
    @Column(nullable = false)
    private int capacity;

    @ElementCollection
    @CollectionTable(name = "room_images", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();



    @OneToMany(mappedBy = "room", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
   // @JsonManagedReference
    private List<Reservation> reservations;

}
