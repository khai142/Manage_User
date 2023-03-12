package com.mk.manageuserpro.service;

import com.mk.manageuserpro.model.User;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserService{

    public User findByUsername(String username);

    public Page<User> getTotalUsers(String name, String groupId, int page);

    public User createUser(User user);

    public Optional<User> findById(Long userId);

    public User editUser(User user, Long userId);

    public void deleteUserById(Long userId);
}
