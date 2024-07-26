package com.example.example5test.security;

import com.example.example5test.entity.User;
import com.example.example5test.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
      User user = userRepository.findByEmail(username)
              .orElseThrow(() -> new UsernameNotFoundException("error username'" + username + "' not found"));
        return UserDetailsImpl.build(user);
    }
}
