package com.example.eCommBackendNew2.service;

import com.example.eCommBackendNew2.dto.UserLoginRequest;
import com.example.eCommBackendNew2.dto.UserLoginResponse;
import com.example.eCommBackendNew2.entity.User;
import com.example.eCommBackendNew2.exception.UserNotFoundException;
import com.example.eCommBackendNew2.repository.UserRepository;
import com.example.eCommBackendNew2.security.JWTService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
//    private UserService userService;
    private JWTService jwtService;
    private PasswordEncoder passwordEncoder;

    private UserRepository userRepository;
    public AuthService(UserRepository userRepository, JWTService jwtService, PasswordEncoder passwordEncoder){
//            this.userService=userService;
            this.userRepository=userRepository;
            this.passwordEncoder=passwordEncoder;
            this.jwtService=jwtService;
    }

    public UserLoginResponse loginCheck(UserLoginRequest userLoginRequest){
        User user=userRepository.findByUsername(userLoginRequest.username()).orElseThrow(()->new UserNotFoundException("user not found"));
        if(!passwordEncoder.matches( userLoginRequest.password(), user.getPassword())){
          throw new BadCredentialsException("invalid username or password");
        }
        String token = jwtService.generateToken(
            user.getId(),
                user.getUsername(),
                user.getRole()
        );
        return new UserLoginResponse(token);
    }
}
