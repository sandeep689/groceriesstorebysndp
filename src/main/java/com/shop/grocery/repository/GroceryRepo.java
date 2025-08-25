package com.shop.grocery.repository;
import com.shop.grocery.entity.GroceryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroceryRepo extends JpaRepository<GroceryEntity, Integer> {
    // No additional methods needed for this use case
}
