package com.example.UberApp.services;

import com.example.UberApp.entities.User;
import com.example.UberApp.exceptions.ResourceNotFoundException;
import com.example.UberApp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public final class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() ->
                new ResourceNotFoundException("User with name not found, name = "+ username));
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User with id not found, id = "+ userId));
    }
}
