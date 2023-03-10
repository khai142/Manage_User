package com.mk.manageuserpro.service;

import com.mk.manageuserpro.model.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService{
    void save(User user);

    public User findByUsername(String username);

    public Page<User> getTotalUsers(String name, String groupId, int page);

    public User saveUser(User user);
}
