package com.shop.grocery.entity;
import java.io.Serializable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Table(name = "grocery")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class GroceryEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE) // or GenerationType.AUTO
    @Column(name = "id")
    private Long id;

    @Column(name = "image", columnDefinition = "TEXT")
    private String image;



    @Column(name = "product")
    private String product;

    @Column(name = "description")
    private String description;

    @Column(name="mrp")
    private int mrp;

    @Column(name="price")
    private int price;

}
