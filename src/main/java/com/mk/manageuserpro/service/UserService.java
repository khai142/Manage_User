package com.mk.manageuserpro.service;

import com.mk.manageuserpro.model.User;

public interface UserService{
    void save(User user);

    User findByUsername(String username);

}
