package com.example.healthadvisors.security;

import com.example.healthadvisors.entity.User;
import com.example.healthadvisors.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userByEmail = userRepository.findUserByEmail(email);
        if(!userByEmail.isPresent()){
            throw new UsernameNotFoundException("User is not found");
        }
        return new CurrentUser(userByEmail.get());
    }
}

