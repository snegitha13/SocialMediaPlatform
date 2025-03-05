//package com.controller;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.ProviderManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.dao.UsersDAO;
//import com.filter.JwtResponse;
//import com.filter.JwtToken;
//import com.model.AuthenticateUser;
//import com.model.Role;
//import com.model.Users;
//
//import org.springframework.security.core.Authentication;
//
//@RestController
//@CrossOrigin("*")
//public class AuthController {
//
//    @Autowired
//    DaoAuthenticationProvider provider;
//    @Autowired
//    UserDAO userRepository;
//
//    public AuthController() {
//        System.out.println("controller invoked");
//    }
//
//    @PostMapping("/api/login")
//    public ResponseEntity<?> authenticate(@RequestBody AuthenticateUser user) {
//        JwtToken jwtToken = new JwtToken();
//        AuthenticationManager manager = new ProviderManager(provider);
//        Authentication authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
//
//        if (authentication.isAuthenticated()) {
//            String username = user.getUsername();
//            String password = user.getPassword();
//            Optional<User> optionalUser = userRepository.findByUsername(username);
//
//            if (optionalUser.isPresent()) {
//                List<Role> roleList = optionalUser.get().getRoles();
//                ResponseEntity<?> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//
//                for (Role r : roleList) {
//                    if (user.getRole().equals(r.getRole_name())) {
//                        System.out.println("Hello");
//                        jwtToken.generateToken(username, password, user.getRole());
//                        response = new ResponseEntity<JwtResponse>(new JwtResponse(jwtToken.getToken()), HttpStatus.ACCEPTED);
//                        break;
//                    }
//                }
//                return response;
//            } else {
//                Map<String, String> errorResponse = new HashMap<>();
//                errorResponse.put("error", "USER_NOT_FOUND");
//                errorResponse.put("message", "User not found");
//                return ResponseEntity.status(404).body(errorResponse);
//            }
//        } else {
//            Map<String, String> errorResponse = new HashMap<>();
//            errorResponse.put("error", "LOGIN_FAILS");
//            errorResponse.put("message", "UNAUTHORIZED");
//            return ResponseEntity.status(403).body(errorResponse);
//        }
//    }
//}