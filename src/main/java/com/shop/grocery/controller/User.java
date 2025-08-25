package com.shop.grocery.controller;
import com.shop.grocery.constant.Constant;
import com.shop.grocery.model.GroceryModel;
import com.shop.grocery.model.UserModel;
import com.shop.grocery.service.JwtService;
import com.shop.grocery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(Constant.USER)
public class User {
    @Autowired
    private UserService userservice;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    JwtService jwtService;

    @PostMapping(value = Constant.REGISTER, produces = "application/json")
    public UserModel registered(@RequestBody UserModel userModel) {
        return userservice.registerUser(userModel);

    }
    @PostMapping(value = Constant.LOGIN, produces = "application/json")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserModel userModel) {
        System.out.println("getting called"+userModel);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userModel.getUsername(), userModel.getPassword()));
        if (authentication.isAuthenticated()) {
            String jwt = jwtService.generateToken(userModel.getUsername());

            // Create HttpOnly, Secure cookie
            ResponseCookie cookie = ResponseCookie.from("token", jwt)
                    .httpOnly(true)
                    .secure(true) // use true in production (HTTPS)
                    .path("/")
                    .sameSite("Strict")
                    .maxAge(24 * 60 * 60) // 1 day
                    .build();

            Map<String, String> response = new HashMap<>();
             response.put("msg", "Login successful");
             response.put("token", jwt); // or cookie.toString() if you want full Set-Cookie string

             return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("msg", "Login failed!!");
            return ResponseEntity.ok(response);
        }

    }

}
