package com.shop.grocery.service;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.shop.grocery.entity.GroceryEntity;
import com.shop.grocery.model.GroceryModel;
import com.shop.grocery.model.GroceryModels;
import com.shop.grocery.model.ImageUploadModel;
import com.shop.grocery.repository.GroceryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GroceryService {
    @Autowired
    private GroceryRepo repo;
    @Value("${app.s3.bucket}")
    private String  bucketName;
@Autowired
    private  AmazonS3 client;



    public GroceryModel addProduct(GroceryModel groceryModel) {
        GroceryEntity entity = new GroceryEntity();
        System.out.println("image abc"+ groceryModel.getImage());
        entity.setImage(groceryModel.getImage());
        entity.setProduct(groceryModel.getProduct());
        entity.setDescription(groceryModel.getDescription());
        entity.setMrp(groceryModel.getMrp());
        entity.setPrice(groceryModel.getPrice());
        GroceryEntity savedEntity = repo.save(entity);
        return new GroceryModel(savedEntity.getImage(), savedEntity.getProduct(), savedEntity.getDescription(), savedEntity.getMrp(), savedEntity.getPrice());
    }

    public List<GroceryModels> getAllItems() {
        List<GroceryEntity> entities = repo.findAll();
        return entities.stream()
                .map(entity -> new GroceryModels(entity.getId(),getUrlByName( entity.getImage()), entity.getProduct(), entity.getDescription(), entity.getMrp(), entity.getPrice()))
                .collect(Collectors.toList());
    }

    public Optional<GroceryModel> getProductById(int id) {
        Optional<GroceryEntity> entityOptional = repo.findById(id);
        return entityOptional.map(entity -> new GroceryModel(
                entity.getImage(),
                entity.getProduct(),
                entity.getDescription(),
                entity.getMrp(),
                entity.getPrice()
        ));
    }

    public Optional<GroceryModel> deleteProductById(int id) {
        Optional<GroceryEntity> entityOptional = repo.findById(id); // Retrieve the entity first
        if (entityOptional.isPresent()) {
            GroceryEntity entity = entityOptional.get();
            repo.deleteById(id); // Now delete the entity
            // Return the deleted entity as a GroceryModel
            return Optional.of(new GroceryModel(
                    entity.getImage(),
                    entity.getProduct(),
                    entity.getDescription(),
                    entity.getMrp(),
                    entity.getPrice()
            ));
        }
        return Optional.empty(); // Return empty if the entity wasn't found
    }

    public ImageUploadModel uploadImage(MultipartFile image) {
        String actualFileName = image.getOriginalFilename();
        String fileName = UUID.randomUUID().toString()+ actualFileName.substring(actualFileName.lastIndexOf("."));
        ObjectMetadata metaData = new ObjectMetadata();
        metaData.setContentLength(image.getSize());
        System.out.println("fileName"+ fileName);
        try {
          PutObjectResult putObjectResult = client.putObject(new PutObjectRequest(bucketName, fileName, image.getInputStream(), metaData));

        }catch (IOException e){
            System.out.println(
                    "Error in uploading Image"+ e
            );
        }
        return new ImageUploadModel(this.preSignedUrl(fileName),fileName);



    }

    public List<String> allFiles(){
        ListObjectsV2Request listRequest = new ListObjectsV2Request().withBucketName(bucketName);
        ListObjectsV2Result listObjectsV2 =  client.listObjectsV2(listRequest);
        List<S3ObjectSummary> objsummaries= listObjectsV2.getObjectSummaries();
        List<String> listFileUrls=objsummaries.stream().map(item-> this.preSignedUrl(item.getKey())).collect(Collectors.toList());
         return listFileUrls;
    }


    public String getUrlByName(String fileName){
       S3Object obj= client.getObject(bucketName, fileName);
       String key = obj.getKey();
       this.preSignedUrl(key);

       return this.preSignedUrl(key);

    }

    public String preSignedUrl(String fileName){
        Date expDate = new Date();
        long time = expDate.getTime();
        time=time + 2*60*60*10000;
        expDate.setTime(time);
        GeneratePresignedUrlRequest  generatePresignedUrl= new GeneratePresignedUrlRequest(bucketName, fileName).withMethod(HttpMethod.GET).withExpiration(expDate);
       URL url= client.generatePresignedUrl(generatePresignedUrl);
        return url.toString().replace("https: //", "https://").trim();
    }
}
