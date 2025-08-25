
package com.shop.grocery.model;
import lombok.*;



@Getter
@Setter
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

public class GroceryModels {
        private Long id;
        private Object image;
        private String product;
        private String description;
        private int mrp;
        private int price;

}
