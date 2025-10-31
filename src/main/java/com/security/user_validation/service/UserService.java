package com.security.user_validation.service;

import com.security.user_validation.model.UserLoginDTO;
import com.security.user_validation.model.Users;
import com.security.user_validation.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserService(UserRepo userRepo,
                       BCryptPasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public boolean verify(UserLoginDTO userDTO) {
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userDTO.getUsername(),
                            userDTO.getPassword()
                    )
            );
            System.out.println(authentication);
            return authentication.isAuthenticated();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public Users updateUser(Users user, String username) {
        Users existingUser = userRepo.findByUsername(username);
        existingUser.setPassword(user.getPassword());
        existingUser.setEmail(user.getEmail());
        existingUser.setUsername(username);
        return userRepo.save(existingUser);
    }

    public void deleteUser(String username) {
        Users existingUser = userRepo.findByUsername(username);
        userRepo.deleteById(existingUser.getId());
    }

    public Users saveUser(Users user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepo.save(user);
    }

    public Users findUser(String username) {
        return userRepo.findByUsername(username);
    }
}
