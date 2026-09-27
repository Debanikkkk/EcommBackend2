package com.example.eCommBackendNew2.service;

import com.example.eCommBackendNew2.dto.RegisterUserRequest;
import com.example.eCommBackendNew2.dto.UserResponse;
import com.example.eCommBackendNew2.entity.User;
import com.example.eCommBackendNew2.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    public UserResponse registerUser(RegisterUserRequest request){
        User user=new User();

        user.setEmail(request.email());
        user.setUsername(request.username());
        user.setPassword(request.password());

        User savedUser=userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getUsername()
        );
    }

    public UserResponse getUserById(Long id){
        User user=userRepository.findById(id)
                .orElseThrow(()->new RuntimeException("User not found"));
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername()
//                user.getPassword()
        );
    }
}
