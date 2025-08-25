package com.shop.grocery.model;


import jakarta.persistence.Column;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ImageUploadModel {

    //@Column(columnDefinition = "TEXT")
    private String presignedUrl;
    private String key;
}

