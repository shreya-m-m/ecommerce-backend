package com.ecommerce.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.backend.response.ApiResponse;

@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public ResponseEntity<ApiResponse> homePage() {
        
        ApiResponse res = new ApiResponse();
        res.setMsg("Welcome to Our Store");
        res.setStatus(true);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
