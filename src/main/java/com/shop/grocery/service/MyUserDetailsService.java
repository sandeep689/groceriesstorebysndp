package com.shop.grocery.service;

import com.shop.grocery.model.UserModel;
import com.shop.grocery.model.UserPrincipal;
import com.shop.grocery.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo repo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userModel= repo.findByUsername(username);
        if(userModel == null){
            System.out.println("User Not found");
            throw new UsernameNotFoundException("User 404");
        }
        return new UserPrincipal(userModel);
    }
}
