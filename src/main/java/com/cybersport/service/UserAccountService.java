package com.cybersport.service;

import com.cybersport.model.UserAccount;
import com.cybersport.repository.UserAccountRepository;

public class UserAccountService {

    private UserAccountRepository userAccountRepository;

    public UserAccountService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public UserAccount createUser(UserAccount user) {
        return userAccountRepository.save(user);
    }

    public UserAccount getUserByUsername(String username) {
        return userAccountRepository.getUserAccountByUsername(username);
    }

    public UserAccount getUserById(Long id) {
        return userAccountRepository.getById(id);
    }
}
