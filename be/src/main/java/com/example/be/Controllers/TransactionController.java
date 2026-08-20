package com.example.be.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class TransactionController {
    
    @GetMapping("/trans")
    public String transactions() {
        return "Transactions endpoint is working!";
    }
}
