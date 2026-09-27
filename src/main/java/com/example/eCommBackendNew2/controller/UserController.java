package com.example.eCommBackendNew2.controller;

import com.example.eCommBackendNew2.dto.RegisterUserRequest;
import com.example.eCommBackendNew2.dto.UserLoginRequest;
import com.example.eCommBackendNew2.dto.UserLoginResponse;
import com.example.eCommBackendNew2.dto.UserResponse;
import com.example.eCommBackendNew2.repository.UserRepository;
import com.example.eCommBackendNew2.service.AuthService;
import com.example.eCommBackendNew2.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
//@Tags(User)
public class UserController {
    private final UserService userService;
    private final AuthService authService;
    public UserController(UserService userService, AuthService authService){
        this.userService=userService;
        this.authService=authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid RegisterUserRequest registerUserRequest){
        UserResponse userResponse=userService.registerUser(registerUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
        UserResponse userById=userService.getUserById(id);

        return ResponseEntity.status(HttpStatus.CREATED).body(userById);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> userLogin(@RequestBody UserLoginRequest userLoginRequest){
            return ResponseEntity.ok(authService.loginCheck(userLoginRequest));
    }
}
