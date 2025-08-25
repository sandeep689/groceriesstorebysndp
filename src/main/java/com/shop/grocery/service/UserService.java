package com.shop.grocery.service;
import com.shop.grocery.model.UserModel;
import com.shop.grocery.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepo repo;
    public UserModel registerUser(UserModel userModel){
        return repo.save(userModel);
    }
}
