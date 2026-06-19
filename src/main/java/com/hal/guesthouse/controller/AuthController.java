package com.hal.guesthouse.controller;

import com.hal.guesthouse.dto.AuthDTO;
import com.hal.guesthouse.model.Employee;
import com.hal.guesthouse.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    // POST /api/login  <-- matches your login.html fetch call exactly
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO.LoginRequest request) {
        AuthDTO.LoginResponse response = authService.login(request);
        // Return just the string "LOGIN_SUCCESS" or "LOGIN_FAILED" to match frontend
        return ResponseEntity.ok(response.getResult());
    }

    // POST /api/login/full  <-- returns full object with role, name etc.
    @PostMapping("/login/full")
    public ResponseEntity<?> loginFull(@RequestBody AuthDTO.LoginRequest request) {
        AuthDTO.LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    // POST /api/register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthDTO.RegisterRequest request) {
        String result = authService.register(request);
        return ResponseEntity.ok(result);
    }

    // GET /api/profile/{pbNo}
    @GetMapping("/profile/{pbNo}")
    public ResponseEntity<?> getProfile(@PathVariable String pbNo) {
        Employee emp = authService.getProfile(pbNo);
        return ResponseEntity.ok(emp);
    }
}
