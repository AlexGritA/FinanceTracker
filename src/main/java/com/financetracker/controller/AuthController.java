package com.financetracker.controller;

import com.financetracker.model.User;
import com.financetracker.security.JwtUtil;
import com.financetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        User existing = userService.findByUsername(user.getUsername());
        if (existing == null) {
            return "User not found";
        }
        if (!passwordEncoder.matches(user.getPassword(), existing.getPassword())) {
            return "Wrong password";
        }
        return jwtUtil.generateToken(existing.getUsername());
    }
}
