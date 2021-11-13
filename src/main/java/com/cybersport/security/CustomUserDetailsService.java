package com.cybersport.security;

import com.cybersport.model.UserAccount;
import com.cybersport.service.UserAccountService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.List;

public class CustomUserDetailsService implements UserDetailsService {

    private UserAccountService userAccountService;

    public CustomUserDetailsService(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountService.getUserByUsername(username);
        List<SimpleGrantedAuthority> roles = Arrays.asList(new SimpleGrantedAuthority(userAccount.getRole().toString()));
        return new User(
                userAccount.getUsername(),
                userAccount.getPassword(),
                roles
        );
    }
}