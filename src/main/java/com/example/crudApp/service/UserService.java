package com.example.crudApp.service;

import com.example.crudApp.model.User;
import com.example.crudApp.model.UserPrincipal;
import com.example.crudApp.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class UserService implements UserDetailsService {
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
     @Autowired
    UserRepo userRepo;

     @Autowired
     @Lazy
    AuthenticationManager authManager;

     @Autowired
     JWTService jwt;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByName(username);
        if(user == null)
        {
            System.out.println("User Not Found ");
            throw new UsernameNotFoundException("User not found");
        }
        return new UserPrincipal(user);
    }
    public User addUser(User user)
    {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public String verify(User user) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getName(),user.getPassword()));
        if(authentication.isAuthenticated())
        {
            return jwt.generateToken(user.getName());
        }
        else
        {
            return "Failed Login";
        }

    }
}

