package com.shop.grocery.repository;
import com.shop.grocery.controller.User;
import com.shop.grocery.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepo extends JpaRepository<UserModel, Integer>{
    UserModel findByUsername(String username);
}

