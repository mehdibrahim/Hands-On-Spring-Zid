package com.zid.zid.controller;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import com.zid.zid.entity.User;
import com.zid.zid.payload.request.LoginRequest;
import com.zid.zid.payload.request.SignupRequest;
import com.zid.zid.payload.response.MessageResponse;
import com.zid.zid.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.zid.zid.security.services.UserDetailsServiceImpl;
import com.zid.zid.security.utils.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;
    @Operation(summary = "User Login",description = "User Login",tags = "Authentication")
    @ApiResponse( responseCode = "200",description="Welecom Into You Account",content = {@Content(mediaType ="application/json")})
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
          //  UserDetails loadedUser = userDetailsServiceImpl.loadUserByUsername(loginRequest.getUsernameOrEmail());
          //  UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String jwt = jwtUtils.generateToken(authentication);
            return ResponseEntity.ok(new MessageResponse(jwt));
        } catch (Exception e) {
            return ResponseEntity.ok(new MessageResponse("Bad credentials"));
        }
        
       
        }

    @Operation(summary = "Register New User",description = "Register New User",tags = "Authentication")
    @ApiResponse( responseCode = "200",description="Welecom Into Our Plateform",content = {@Content(mediaType ="application/json")})
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest){


        if(userRepository.existsByUsername(signUpRequest.getUsername())){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));

        }


        if(userRepository.existsByEmail(signUpRequest.getEmail())){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already taken!"));
        }


        User user = new User();
        user.setName(signUpRequest.getName());
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));



        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));

    }

}
