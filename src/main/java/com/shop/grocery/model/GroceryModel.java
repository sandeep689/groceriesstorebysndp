package com.shop.grocery.model;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroceryModel {

    //@Column(columnDefinition = "TEXT")
    private String image;
    private String product;
    private String description;
    private int mrp;
    private int price;
}
