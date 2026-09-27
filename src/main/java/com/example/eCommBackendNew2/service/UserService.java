package com.example.eCommBackendNew2.service;

import com.example.eCommBackendNew2.dto.RegisterUserRequest;
import com.example.eCommBackendNew2.dto.UserResponse;
import com.example.eCommBackendNew2.entity.User;
import com.example.eCommBackendNew2.exception.UserNotFoundException;
import com.example.eCommBackendNew2.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
    }

    public UserResponse registerUser(RegisterUserRequest request){
        User user=new User();

        user.setEmail(request.email());
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole("USER");
        User savedUser=userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getUsername(),
                savedUser.getRole()
        );
    }

    public UserResponse getUserById(Long id){
        User user=userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("User not found"));
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getRole()
//                user.getPassword()
        );
    }

    public UserResponse getUserByUsername(String username){
        User user=userRepository.findByUsername(username)
                .orElseThrow(()->new UserNotFoundException("User not found"));
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getRole()
//                user.getPassword()
        );
    }
}
