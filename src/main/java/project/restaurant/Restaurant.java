package project.restaurant;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String foodName;

    @Column(nullable = false)
    private String photoURL;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @NotNull
    private Double price;
}
