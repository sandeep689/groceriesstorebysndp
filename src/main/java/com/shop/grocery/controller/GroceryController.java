package com.shop.grocery.controller;
import com.shop.grocery.constant.Constant;
import com.shop.grocery.model.GroceryModel;
import com.shop.grocery.model.GroceryModels;
import com.shop.grocery.model.ImageUploadModel;
import com.shop.grocery.service.GroceryService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(Constant.GROCERIES)
public class GroceryController {
    @Autowired
    private GroceryService service;

    @Autowired
    private ImageUploadModel imageUploadModel;

    @PostMapping(
            value = Constant.ADD_ITEM,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = "application/json"
    )
    public ResponseEntity<?> addItem(
            @RequestParam("image") MultipartFile image,
            @RequestParam("product") String product,
            @RequestParam("description") String description,
            @RequestParam("mrp") int mrp,
            @RequestParam("price") int price
    ) {
        GroceryModel groceryModel = new GroceryModel();
        groceryModel.setImage( service.uploadImage(image).getKey());
        groceryModel.setProduct(product);
        groceryModel.setDescription(description);
        groceryModel.setMrp(mrp);
        groceryModel.setPrice(price);
        GroceryModel saved = service.addProduct(groceryModel);
        return ResponseEntity.ok(saved);
    }

    @PostMapping(value = Constant.IMAGE_UPLOAD, produces = "application/json")
    public ResponseEntity<?> uploadImage(@RequestParam MultipartFile file) {
        return ResponseEntity.ok(service.uploadImage(file));
    }

    @GetMapping(value = Constant.IMAGE + "/{fileName}", produces = "application/json")
    public ResponseEntity<String> singleImage(@PathVariable String fileName) {
        String presignedUrl = service.getUrlByName(fileName); // assuming method name is correct
        System.out.println("presignedUrl"+presignedUrl);
        return ResponseEntity.ok(presignedUrl);
    }


    @GetMapping(value = Constant.ALL_IMAGE, produces = "application/json")
    public ResponseEntity<List<String>> getAllImage() {
        return ResponseEntity.ok(service.allFiles());
    }

    @GetMapping(value = Constant.GROCERY_ITEMS,  produces = "application/json")
    public List<GroceryModels> getAllItems() {
        return service.getAllItems();
    }
    @Cacheable(value = "groceryItem")
    @GetMapping(value = Constant.PRODUCT_ID, produces = "application/json")
    public Optional<GroceryModel> getProductById(@PathVariable int id) {
        return service.getProductById(id);
    }
    @DeleteMapping(value = Constant.PRODUCT_ID, produces = "application/json")
    public Optional<GroceryModel> deleteProductById(@PathVariable int id){
        return service.deleteProductById(id);

    }
}

